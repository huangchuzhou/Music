package com.zdxh.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zdxh.music.R;
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


        searchListView.setOnItemClickListener(this);
    }



    //初始化DataBean这个实体类
    private void initDataBean() {

        String searchName = getIntent().getStringExtra("searchName");
        data[3] = searchName;
        Search mSearch = new Search(searchName);

        mSearch.getEntityBeanData(getApplicationContext(),new EntityBeanBackListener() {
            @Override
            public void info(ArrayList<EntityBean> entityBeans) {
                entityBeanArrayList = entityBeans;

            }
        });


        mSearch.getImageBeanData(getApplicationContext(), new ImageBeanCallBackListener() {
            @Override
            public void info(String imageUrl, ImageBean mImageBean) {
                //根据singerPicUrl查询数据库中是否有这条数据
                if (!MusicDB.musicDB.isExistsImage(imageUrl)){
                    //加载网络图片
                    Bitmap bitmap = getHttpBitmap(mImageBean.getData().getSingerPic());
                    //先判断数据库中是否存在这张图片
                    //存储这张图片
                    MusicDB.musicDB.saveImageDataBean(imageUrl, bitmap);
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
        EntityBean.DataBean mDataBean = dataBeanList.get(position);
        /**
         * 如果当前加载的图片是默认的图片则从网络上获取图片
         *加载网络图片先获取ListView item 的歌手名字
         */
//        if ( !Boolean.valueOf(album.getResources().getResourceName(R.drawable.main_album))){
//            String songName = mDataBean.getSong_name();
//            Search mSearch = new Search(songName);
//            mSearch.getImageBeanData(getApplicationContext(), new ImageBeanCallBackListener() {
//                @Override
//                public void info(ImageBean mImageBean) {
//                    //加载网络图片
//                    Bitmap bitmap = getHttpBitmap(mImageBean.getData().getSingerPic());
//                    Log.d("TAG",bitmap.toString());
//                    album.setImageBitmap(bitmap);
//                }
//
//            });
//        }


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
        isPause = true;
        startService(intent);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(SearchListAty.this, MediaService.class);
        if (isPause == true){
            btnPlay.setImageResource(R.drawable.icon_play_normal);
            isPause = false;

            intent.putExtra("songUrl",songUrl);
            intent.setAction(MediaService.PLAY);
            startService(intent);
        }else {
            btnPlay.setImageResource(R.drawable.icon_pause_normal);
            isPause = true;
            intent.putExtra("songUrl",songUrl);
            intent.setAction(MediaService.PAUSE);
            startService(intent);
        }


    }

    public static String[] returnData()
    {
        return data;
    }
}

