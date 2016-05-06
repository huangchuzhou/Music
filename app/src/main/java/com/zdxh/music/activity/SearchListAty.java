package com.zdxh.music.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.zdxh.music.R;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.bean.ImageBean;
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.DataBeanAdapter;
import com.zdxh.music.util.EntityBeanBackListener;
import com.zdxh.music.util.ImageBeanCallBackListener;
import com.zdxh.music.util.Search;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 */
public class SearchListAty extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView searchListView;
    private DataBeanAdapter adapter;

    private ImageButton btnPlay;
    private ImageButton btnLove;
    private ImageView album;
    private TextView tvSingerName;
    private TextView tvSongName;
    private String songUrl;



    //返回到LRCFragment的数据有数组包装（SingerName SongName SongUrl）
    private static String[] data = new String[4];
    public static boolean isPause = false;
    public static boolean isLove = false;
    public FinishReceiver mFinishReceiver = null;
    private ArrayList<EntityBean> entityBeanArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_aty_layout);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnLove = (ImageButton) findViewById(R.id.btnLove);

        album = (ImageView) findViewById(R.id.album);
        tvSingerName = (TextView) findViewById(R.id.tvSingerName);
        tvSongName = (TextView) findViewById(R.id.tvSongName);

        searchListView = (ListView) findViewById(R.id.search_listView);



        initDataBean();
        /**
         * 主线程休眠1s,使得另一个线程先执行，dataBeanList才能被重新赋值
         * 这里的另一个线程指的是对网络数据的读取而开启的线程
         */


        try {
            Thread.sleep(1000);
            adapter = new DataBeanAdapter(SearchListAty.this,R.layout.search_list_aty_item, entityBeanArrayList);

            searchListView.setAdapter(adapter);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btnPlay.setOnClickListener(this);
        btnLove.setOnClickListener(this);

        searchListView.setOnItemClickListener(this);

        //注册finish广播接收器
        mFinishReceiver = new FinishReceiver();
        IntentFilter filter = new IntentFilter(MediaService.RECEIVERFINISH);
        registerReceiver(mFinishReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFinishReceiver);
    }

    //初始化DataBean这个实体类
    private void initDataBean() {

        String searchName = getIntent().getStringExtra("searchName");

        Search mSearch = new Search(searchName);

        mSearch.getEntityBeanData(getApplicationContext(),new EntityBeanBackListener() {
            @Override
            public void info(ArrayList<EntityBean> entityBeans) {
                entityBeanArrayList = entityBeans;

            }
        });


        mSearch.getImageBeanData(getApplicationContext(), new ImageBeanCallBackListener() {
            @Override
            public void info(String imageUrl,String singerName, ImageBean mImageBean) {
                //根据singerPicUrl查询数据库中是否有这条数据
                if (!MusicDB.musicDB.isExistsImage(imageUrl)){
                    //加载网络图片
                    Bitmap bitmap = getHttpBitmap(mImageBean.getData().getSingerPic());
                    //先判断数据库中是否存在这张图片
                    //存储这张图片
                    MusicDB.musicDB.saveImageDataBean(imageUrl,singerName, bitmap);
                    album.setImageBitmap(bitmap);
                }else {
                    album.setImageBitmap(MusicDB.musicDB.loadImage(imageUrl));
                }
            }
        });


    }
    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){

        URL myFileURL;
        Bitmap bitmap=null;
        HttpURLConnection conn = null;
        try{
            myFileURL = new URL(url);
            //获得连接
            conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为2000毫秒，conn.setConnectionTime(0);表示没有时间限制
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            conn.disconnect();
        }

        return bitmap;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        EntityBean mEntityBean = entityBeanArrayList.get(position);

        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
        final EntityBean.DataBean mDataBean = dataBeanList.get(position);

        MusicApplication.getEnjoy().add(mDataBean);


        if (MusicApplication.getEnjoy().size()>0){
            Intent intent = new Intent();
            intent.setAction(MediaService.ENJOYPLAY);
            sendBroadcast(intent);
        }



        String singerName = mDataBean.getSinger_name();
        Search mSearch = new Search(singerName);
        mSearch.getImageBeanData(SearchListAty.this, new ImageBeanCallBackListener() {
            @Override
            public void info(final String imageUrl, final String singerName, final ImageBean mImageBean) {
                if ( !MusicDB.musicDB.isExistsImage(imageUrl)){

                    if (mImageBean.getCode()==1){

                        ImageRequest mImageRequest = new ImageRequest(mImageBean.getData().getSingerPic(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        album.setImageBitmap(bitmap);
                                        MusicDB.musicDB.saveImageDataBean(imageUrl,singerName,bitmap);
                                    }
                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //获取不到图片

                            }
                        });
                        MusicApplication.getRequestQueue().add(mImageRequest);
                    }else {
                        album.setImageResource(R.drawable.music_icon);
                    }
                }else {
                    album.setImageBitmap(MusicDB.musicDB.loadImage(imageUrl));
                }
            }
        });

        //获取EntityBean.DataBean.AuditionListBean的songUrl


        tvSingerName.setText(mDataBean.getSinger_name());
        data[0] = mDataBean.getSinger_name();  //data数组第一项保存SingerName

        tvSongName.setText(mDataBean.getSong_name());
        data[1] = mDataBean.getSong_name();  //data数组第二项保存SongName

        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();

        EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(position);

        songUrl = mAuditionListBean.getUrl();
        data[2] = songUrl;   //data数组第三项保存songUrl

        //传递songUrl给MediaService
        Intent intent = new Intent(SearchListAty.this, MediaService.class);
        intent.putExtra("songUrl",songUrl);
        intent.setAction(MediaService.PLAY);
        btnPlay.setImageResource(R.drawable.icon_pause_normal);
        isPause = false;
        startService(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlay:
                Intent intent = new Intent(SearchListAty.this, MediaService.class);
                if (isPause == false){
                    btnPlay.setImageResource(R.drawable.icon_play_normal);
                    isPause = true;

                    intent.putExtra("songUrl",songUrl);
                    intent.setAction(MediaService.PAUSE);
                    startService(intent);
                }else {
                    btnPlay.setImageResource(R.drawable.icon_pause_normal);
                    isPause = false;
                    intent.putExtra("songUrl",songUrl);
                    intent.setAction(MediaService.PLAY);
                    startService(intent);
                }

                break;

            case R.id.btnLove:
                if (isLove == false){
                    btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
                    isLove = true;
                }else {
                    btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
                    isLove = false;
                }
                break;
        }
    }

    public static String[] returnData()
    {
        return data;
    }


    class FinishReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MediaService.RECEIVERFINISH)){
                btnPlay.setImageResource(R.drawable.icon_play_normal);
            }
        }
    }


}

