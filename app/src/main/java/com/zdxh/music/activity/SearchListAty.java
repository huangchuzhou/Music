package com.zdxh.music.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.zdxh.music.util.DownloadMp3;
import com.zdxh.music.util.EntityBeanBackListener;
import com.zdxh.music.util.ImageBeanCallBackListener;
import com.zdxh.music.util.Search;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 */
public class SearchListAty extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView searchListView;
    private DataBeanAdapter adapter;
    private ImageButton btnPlay,btnPrevious,btnNext;
    //    private ImageButton btnLove;
    private ImageView album;
    private TextView tvSingerName;
    private TextView tvSongName;
    private String songUrl;

    //返回到LRCFragment的数据有数组包装（SingerName SongName SongUrl）
    private static String[] data = new String[3];
    public static boolean isPause = false;
    public FinishReceiver mFinishReceiver = null;
    private ArrayList<EntityBean> entityBeanArrayList = new ArrayList<>();
    public static int currentPosition = -1;

    private TextView networkInfo; //网络连接状态信息
    private int databeansSize = -1;
//    private static EntityBean.DataBean mDataBean;

    //标志远程或者本地
    public static final String LOCAL = "LOCAL";
    public static final String REMOTE = "REMOTE";
    private File file = null; //本地MP3文件
    private String songUrlPre = null;  //获取前一首歌的url
    private String songUrlNext = null; //获取下一首歌的url
    private static EntityBean.DataBean mDataBean;

    //异步处理数据
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x001){
                EntityBean.DataBean dataBean = (EntityBean.DataBean)msg.obj;
                //判断当前的歌手数据库中是否有其图片
                String singerName = dataBean.getSinger_name();
                Log.d("TAG","singerName1111111111:"+singerName);
                if (MusicDB.musicDB.isExistsImage(singerName)){
                    album.setImageBitmap(MusicDB.musicDB.loadImage(singerName));
                    Log.d("TAG","singerName22222222:"+singerName);
                }else {
                    album.setImageResource(R.drawable.music_icon);
                }
                tvSingerName.setText(dataBean.getSinger_name());
                tvSongName.setText(dataBean.getSong_name());
                songUrlPre = msg.getData().getString("songUrlPre");

            }else if (msg.what == 0x002){
                EntityBean.DataBean dataBean = (EntityBean.DataBean) msg.obj;
                //判断当前的歌手数据库中是否有其图片
                String singerName = dataBean.getSinger_name();
                if (MusicDB.musicDB.isExistsImage(singerName)){
                    album.setImageBitmap(MusicDB.musicDB.loadImage(singerName));
                }else {
                    album.setImageResource(R.drawable.music_icon);
                }
                tvSongName.setText(dataBean.getSong_name());
                tvSingerName.setText(dataBean.getSinger_name());
                songUrlNext = msg.getData().getString("songUrlNext");

            }else if (msg.what == 0x003){
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap==null){
                    album.setImageResource(R.drawable.music_icon);
                }else {
                    album.setImageBitmap(bitmap);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MusicApplication.isShowOther = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_aty_layout);

        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);

        btnPlay.setEnabled(false);
        btnPrevious.setEnabled(false);
        btnNext.setEnabled(false);

        album = (ImageView) findViewById(R.id.album);
        tvSingerName = (TextView) findViewById(R.id.tvSingerName);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        searchListView = (ListView) findViewById(R.id.search_listView);
        networkInfo = (TextView) findViewById(R.id.networkInfo);

        //判断网络是否可达
        if (MusicApplication.isNetworkAvailable){
            searchListView.setVisibility(View.VISIBLE);
            networkInfo.setVisibility(View.INVISIBLE);
        }


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
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        searchListView.setOnItemClickListener(this);
        searchListView.setOnItemLongClickListener(this);
//        searchListView.setOnScrollChangeListener(this);
        //注册finish广播接收器
        mFinishReceiver = new FinishReceiver();
        IntentFilter filter = new IntentFilter(MediaService.RECEIVERFINISH);
        registerReceiver(mFinishReceiver,filter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicApplication.isShowOther = false;
        unregisterReceiver(mFinishReceiver);
        LayoutInflater inflater = LayoutInflater.from(SearchListAty.this);
        View view = inflater.inflate(R.layout.search_list_aty_item,null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layout_other);
        linearLayout.setVisibility(View.INVISIBLE);
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
        currentPosition = position;
        MusicApplication.isShowOther = false; //关闭other页面
        MusicApplication.isFromSearchListAty = true;
        EntityBean mEntityBean = entityBeanArrayList.get(position);
        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
        mDataBean = dataBeanList.get(position);
        MusicApplication.dataBeans.add(mDataBean);
        //判断当前点击的歌是否有被收藏
        for (EntityBean.DataBean databean : MusicApplication.collections) {
            if (databean == mDataBean){
                MusicApplication.isCollection = true; //有被收藏
                //发送广播
                Intent intent = new Intent();
                intent.setAction(MediaService.COLLECTION);
                sendBroadcast(intent);
                break;
            }else {
                MusicApplication.isCollection = false; //没有被收藏
                //发送广播
                Intent intent = new Intent();
                intent.setAction(MediaService.COLLECTION);
                sendBroadcast(intent);
            }
        }

        databeansSize = dataBeanList.size();
        //先判断在内存card中是否有相应的歌曲
        file = new File(DownloadMp3.DOWNLOAD_PATH,mDataBean.getSong_id()+".mp3");
        if(file.exists()){
            //从本地获取Mp3文件
            getMp3FromLocal(mDataBean,position,file,LOCAL);
        }else {
            //从远程获取Mp3文件
            getMp3FromRemote(mDataBean,position,REMOTE);
        }

    }

    private void getMp3FromLocal(EntityBean.DataBean dataBean,int position, File file, String local) {

        //初始化视图
        initView(position,local);
        //传递songUrl给MediaService
        sendSongUrlToService(dataBean,file.toString());
    }

    private void getMp3FromRemote(EntityBean.DataBean dataBean,int position, String remote) {
        //网络可用
        if (MusicApplication.isNetworkAvailable){
            //初始化视图
            initView(position,remote);
            //传递songUrl给MediaService
            sendSongUrlToService(dataBean,songUrl);
        }else {   //网络不用
            Toast.makeText(this,"木有网络...",Toast.LENGTH_SHORT).show();
        }


//        if (MusicApplication.isCollection==true){
//            btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
//
//        }else{
//            btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
//        }
    }


    private void initView(int position,String from) {
        btnPlay.setEnabled(true);

        EntityBean mEntityBean = entityBeanArrayList.get(position);

        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
        mDataBean = dataBeanList.get(position);

        btnPrevious.setEnabled(true);
        btnNext.setEnabled(true);
        MusicApplication.getEnjoy().add(mDataBean);
        if (++MusicApplication.flag<3) {
            MusicApplication.songUrlLocation[MusicApplication.flag] = position;
        }

//        Iterator<EntityBean.DataBean> iter = MusicApplication.getEnjoy().iterator();
//        while (iter.hasNext()){
//            if (mDataBean.equals(iter.next())){
//                iter.remove();
//                MusicApplication.flag --;
//            }
//        }



        if (MusicApplication.getEnjoy().size()>0){
            Intent intent = new Intent();
            intent.setAction(MediaService.ENJOYPLAY);
            sendBroadcast(intent);
        }

        //加载图片
        String singerName = mDataBean.getSinger_name();
        //数据库中存在图片
        isExisitsImage(singerName);
        //数据库中不存在图片,从网络加载图片
        loadImageFromNet(singerName);

        tvSingerName.setText(mDataBean.getSinger_name());
        data[0] = mDataBean.getSinger_name();  //data数组第一项保存SingerName

        tvSongName.setText(mDataBean.getSong_name());
        data[1] = mDataBean.getSong_name();  //data数组第二项保存SongName

        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();
        //远程
        if (from.equals(REMOTE)){
            EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(position);

            songUrl = mAuditionListBean.getUrl();
            data[2] = songUrl;   //data数组第三项保存songUrl
        }else if (from.equals(LOCAL)){   //本地
            songUrl = file.getAbsolutePath(); //获取MP3文件的路径
            data[2] = songUrl;
        }

    }


    //数据库中存在图片
    private void isExisitsImage(String singerName) {
        if (MusicDB.musicDB.isExistsImage(singerName)){
            Bitmap bitmap = MusicDB.musicDB.loadImage(singerName);
            ImageThread imageThread = new ImageThread(bitmap);
            imageThread.start();
        }
    }
    //数据库中不存在图片,从网络加载图片
    private void loadImageFromNet(String singerName) {
        if (!MusicDB.musicDB.isExistsImage(singerName)){
            Search search = new Search(singerName);
            search.getImageBeanData(this, new ImageBeanCallBackListener() {
                @Override
                public void info(final String imageUrl, final String singerName, ImageBean mImageBean) {

                    if (mImageBean.getCode() == 2){ //返回码为2，没有图片资源
                        ImageThread imageThread = new ImageThread(null);
                        imageThread.start();
                    }else {   //有图片资源
                        ImageRequest mImageRequest = new ImageRequest(mImageBean.getData().getSingerPic(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        album.setImageBitmap(bitmap);
                                        //保存到数据库中
                                        MusicDB.musicDB.saveImageDataBean(imageUrl,singerName,bitmap);
                                    }
                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //获取不到图片

                            }
                        });
                        MusicApplication.getRequestQueue().add(mImageRequest);
                    }
                }
            });
        }
    }

//    @Override
//    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        MusicApplication.isShowOther = false;
//    }

    //处理图片的线程
    class ImageThread extends Thread{
        Bitmap bitmap;
        public ImageThread(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            super.run();
            Message msg = mHandler.obtainMessage();
            msg.obj = bitmap;
            msg.what = 0x003;
            mHandler.sendMessage(msg);
        }
    }


    private void sendSongUrlToService(EntityBean.DataBean dataBean,String songUrl) {
        //传递songUrl给MediaService
        Intent intent = new Intent(SearchListAty.this, MediaService.class);
        intent.putExtra("songUrl",songUrl);
        Bundle bundle = new Bundle();
        bundle.putSerializable("databean",dataBean);
        intent.putExtras(bundle);
        intent.setAction(MediaService.PLAY);
        btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
        isPause = false;
        startService(intent);
    }


    public static String[] returnData()
    {
        return data;
    }

    public static EntityBean.DataBean returnDataBean()
    {
        return mDataBean;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        MusicApplication.isShowOther = true; //展示Other页面
        currentPosition = position;
        EntityBean mEntityBean = entityBeanArrayList.get(position);
        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
        EntityBean.DataBean dataBean = dataBeanList.get(position);
        LayoutInflater inflater = LayoutInflater.from(SearchListAty.this);
//        View mView = inflater.inflate(R.layout.search_list_aty_item,null);
//        LinearLayout collection = (LinearLayout) mView.findViewById(R.id.item_collection);
//        collection.setOnClickListener(this);
//        if (isCollection==true){
//            //收藏这首歌
//            MusicApplication.getDataBeensCollection().add(dataBean);
//            btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
//
////            isLove = true;
//        }else{
//            btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
////            isLove = false;
//        }
        adapter.notifyDataSetChanged();
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnPlay:

                Intent intentPlay = new Intent(SearchListAty.this, MediaService.class);
                if (isPause == false){
                    btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                    isPause = true;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("databean",mDataBean);
                    intentPlay.putExtras(bundle);
                    intentPlay.putExtra("songUrl",songUrl);
                    intentPlay.setAction(MediaService.PAUSE);
                    startService(intentPlay);
                }else {
                    btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                    isPause = false;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("databean",mDataBean);
                    intentPlay.putExtras(bundle);
                    intentPlay.putExtra("songUrl",songUrl);
                    intentPlay.setAction(MediaService.RESUME);
                    startService(intentPlay);
                }
                break;
            case R.id.btnPrevious:

                //区别各种情况判断btnPrevious btnNext 是否可以点击
                if (currentPosition != 0){
                    Intent intentPrevious = new Intent(SearchListAty.this, MediaService.class);
                    if (isPause == false){
                        initView(currentPosition,REMOTE);

                        btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                        //获取前一首歌的url
                        EntityBean.DataBean dataBean = getPreSongUrl(currentPosition);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",dataBean);
                        intentPrevious.putExtras(bundle);

                        intentPrevious.putExtra("songUrl",songUrlPre);
                        intentPrevious.setAction(MediaService.PLAY);
                        startService(intentPrevious);
                    }
                    currentPosition--;
                }else {
                    Toast.makeText(SearchListAty.this,"已经是第一首歌了",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnNext:

                if (currentPosition+1 <databeansSize){
                    Log.d("TAG","btnNext");
                    Intent intentNext = new Intent(SearchListAty.this, MediaService.class);
                    if (isPause == false) {
                        initView(currentPosition,REMOTE);
                        btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);

                        //获取下一首歌的url
                        EntityBean.DataBean dataBean = getNextSongUrl(currentPosition);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",dataBean);
                        intentNext.putExtras(bundle);

                        intentNext.putExtra("songUrl", songUrlNext);
                        intentNext.setAction(MediaService.PLAY);
                        startService(intentNext);
                    }
                    currentPosition++;
                }else {
                    Toast.makeText(SearchListAty.this,"已经是最后一首歌了",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //处理songUrl的线程（前一首歌，后一首歌）
    class SongUrlThread extends Thread{
        EntityBean.DataBean dataBean;
        public SongUrlThread(EntityBean.DataBean dataBean) {
            this.dataBean = dataBean;
        }

        @Override
        public void run() {
            super.run();
            Message msgPre = mHandler.obtainMessage();
            msgPre.what = 0x001;
            msgPre.obj = dataBean;
            mHandler.sendMessage(msgPre);

            Message msgNext = mHandler.obtainMessage();
            msgNext.what = 0x002;
            msgNext.obj = dataBean;
            mHandler.sendMessage(msgNext);

        }
    }
    private EntityBean.DataBean getNextSongUrl(int currentPosition) {

        EntityBean mEntityBeanNext = entityBeanArrayList.get(currentPosition);
        List<EntityBean.DataBean> dataBeanListNext = mEntityBeanNext.getData();
        final EntityBean.DataBean dataBeanNext = dataBeanListNext.get(currentPosition+1);
        List<EntityBean.DataBean.AuditionListBean> auditionListBeanListNext = dataBeanNext.getAudition_list();
        //后一首歌
        EntityBean.DataBean.AuditionListBean auditionListBeanNext = auditionListBeanListNext.get(++currentPosition);
        songUrlNext = auditionListBeanNext.getUrl();
        Log.d("TAG",dataBeanNext.toString());
        SongUrlThread threadNext = new SongUrlThread(dataBeanNext);
        threadNext.start();

        return dataBeanNext;
    }

    private EntityBean.DataBean getPreSongUrl(int currentPosition) {
        EntityBean mEntityBean = entityBeanArrayList.get(currentPosition);
        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
        final EntityBean.DataBean dataBean = dataBeanList.get(currentPosition-1);
        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = dataBean.getAudition_list();
        //前一首歌
        EntityBean.DataBean.AuditionListBean auditionListBeanPre = auditionListBeanList.get(--currentPosition);
        songUrlPre = auditionListBeanPre.getUrl();

        SongUrlThread threadPre = new SongUrlThread(dataBean);
        Log.d("TAG",dataBean.toString());
        threadPre.start();

        return dataBean;
    }


    class FinishReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MediaService.RECEIVERFINISH)){
                btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
            }
        }
    }



}
