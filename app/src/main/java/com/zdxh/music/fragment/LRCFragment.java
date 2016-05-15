package com.zdxh.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.mp3.LrcView;
import com.zdxh.music.service.MediaService;

import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 听歌页面
 */
public class LRCFragment extends Fragment {
    private View view;
    private ReceiverPlayReceiver receiverPlayReceiver;
    private CollectionReceiver collectionReceiver;
    private ImageButton btnPlay,btnLove;
    private TextView tvSongName,tvSingerName;
    private String[] data;
    private ImageView album;
    public static LrcView lrcView; // 自定义歌词视图
    private ArrayList<ArrayList> queues = null;
    public static TextView mp3TitleTextView = null;
//    private UpdateTimeCallback updateTimeCallback = null;
    private long begin = 0;
    private long nextTimeMill = 0;
    private long currentTimeMill = 0;
    private long pauseTimeMills = 0;
    private long songtime = 0;
    private String message = null;
    private Handler handler = new Handler();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取广播的实例

        IntentFilter receiverPlayFilter = new IntentFilter();
        receiverPlayFilter.addAction(MediaService.RECEIVERPLAY); //最近播放广播
        receiverPlayReceiver = new ReceiverPlayReceiver();
        getActivity().registerReceiver(receiverPlayReceiver,receiverPlayFilter);


        IntentFilter collectionFilter = new IntentFilter();
        collectionFilter.addAction(MediaService.COLLECTION); //收藏广播
        collectionReceiver = new CollectionReceiver();
        getActivity().registerReceiver(collectionReceiver,collectionFilter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.lrc_fragment_layout,container,false);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        btnLove = (ImageButton) view.findViewById(R.id.btnLove);
        tvSingerName = (TextView) view.findViewById(R.id.tvSingerName);
        tvSongName = (TextView) view.findViewById(R.id.tvSongName);
        album = (ImageView) view.findViewById(R.id.album);
        lrcView = (LrcView) view.findViewById(R.id.lrcShowView);
        mp3TitleTextView = (TextView)view.findViewById(R.id.mp3TitleText);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class CollectionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if (MusicApplication.isCollection&&MediaService.isPlay){
                btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
            }else {
                btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
            }
        }
    }
    class ReceiverPlayReceiver extends BroadcastReceiver implements View.OnClickListener {

        @Override
        public void onReceive(Context context, Intent intent) {

            data = SearchListAty.returnData();

            if (MediaService.RECEIVERPLAY.equals(intent.getAction())&&MediaService.isFinish==false){

                //从活动中获取相应Fragment的实例 更改button 的样式
                btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                SearchListAty.isPause = false;
                btnPlay.setOnClickListener(this);
                btnLove.setOnClickListener(this);
                if (MusicApplication.isFromSearchListAty == true){
                    tvSingerName.setText(data[0]);
                    tvSongName.setText(data[1]);
                }else {
                    if (MusicApplication.data[0]!=null){
                        tvSingerName.setText(MusicApplication.data[0]);
                        tvSongName.setText(MusicApplication.data[1]);
                    }
                }

                //获取lrc文件
                int lrcId = MusicDB.musicDB.getLrcId(tvSongName.getText().toString(),tvSingerName.getText().toString());
                Log.d("lrcId",lrcId+"");
                //处理lrc文件
                if (lrcId != -1){
//                    prepareLrc(String.valueOf(lrcId));

                    //将begin的值置为当前毫秒数
//                    begin = System.currentTimeMillis();
                    //执行updateTimeCallback
//                    handler.postDelayed(updateTimeCallback, 5);
                }

                String singerName = tvSingerName.getText().toString();
                if (MusicDB.musicDB.isExistsImage(singerName)){
                    //从数据库中加载图片
                    album.setImageBitmap(MusicDB.musicDB.loadImage(singerName));
                }else {
                    album.setImageResource(R.drawable.music_icon);
                }


            }else {
                btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                MediaService.isFinish = false;
                SearchListAty.isPause = true;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnPlay:
                    if (SearchListAty.isPause == false){
                        btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                        SearchListAty.isPause = true;

                        Intent intentPause = new Intent(getActivity(),MediaService.class);
                        intentPause.putExtra("songUrl",data[2]);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",SearchListAty.returnDataBean());
                        intentPause.putExtras(bundle);
                        intentPause.setAction(MediaService.PAUSE);
//                        handler.removeCallbacks(updateTimeCallback);
//                        pauseTimeMills = System.currentTimeMillis();
                        getActivity().startService(intentPause);

                    }else if (SearchListAty.isPause == true){
                        btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                        SearchListAty.isPause = false;
                        Intent intentPlay = new Intent(getActivity(),MediaService.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",SearchListAty.returnDataBean());
                        intentPlay.putExtras(bundle);
                        intentPlay.putExtra("songUrl",data[2]);
                        intentPlay.setAction(MediaService.RESUME);
//                        //获取lrc文件
//                        int lrcId = MusicDB.musicDB.getLrcId(tvSongName.getText().toString(),tvSingerName.getText().toString());
//                        //处理lrc文件
//                        if (lrcId != -1){
//                            prepareLrc(String.valueOf(lrcId));
//
//                            //将begin的值置为当前毫秒数
//                            begin = System.currentTimeMillis();
//                            //执行updateTimeCallback
//                            handler.postDelayed(updateTimeCallback, 5);
//                        }

//                        begin = System.currentTimeMillis()  - pauseTimeMills + begin;
//                        handler.postDelayed(updateTimeCallback, 5);
                        getActivity().startService(intentPlay);
                    }
                    break;
                case R.id.btnLove:
                    if (MusicApplication.isCollection == false){
                        btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
                        MusicApplication.isCollection = true;
                    }else {
                        btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
                        MusicApplication.isCollection = false;
                    }
                    break;
            }

        }
    }

    //动态取消注册
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverPlayReceiver);
        getActivity().unregisterReceiver(collectionReceiver);
    }

    /**
     * 根据歌词文件的名字，来读取歌词文件当中的信息
     * @param lrcName */
//
//    private void prepareLrc(String lrcName){
//
//        try {
//            InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsoluteFile()
//                    + File.separator + "downloads/lrc/" + lrcName+".lrc");
//
//            LrcProcess lrcProcessor = new LrcProcess();
//
//            queues = lrcProcessor.process(inputStream);
//            ArrayList mp3Titles = queues.get(2);
//            mp3TitleTextView.setText("歌曲名：" + mp3Titles.get(0) + " \n" +"词曲作者：" + mp3Titles.get(1) + "\n" + "专辑名：" + mp3Titles.get(2));
//            //创建一个UpdateTimeCallback对象
//            updateTimeCallback = new UpdateTimeCallback(queues);
//
//            begin = 0;
//            currentTimeMill = 0;
//            nextTimeMill = 0;
//        }catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//
//
//    }
//
//    class UpdateTimeCallback implements Runnable{
//
//        ArrayList times = null;
//        ArrayList messages = null;
//        int i = 0;
//        public UpdateTimeCallback(ArrayList<ArrayList> queues){
//            //从ArrayList当中取出相应的对象对象
//            times = queues.get(0);
//            messages = queues.get(1);
//
//
//        }
//        public void run(){
//            //计算偏移量，也就是说从开始播放Mp3到现在为止，共消耗了多少时间
//            int ss = times.size();
//
//            long offset = System.currentTimeMillis() - begin;
//            if (currentTimeMill == 0){
//                nextTimeMill = (Long)times.get(i);
//                message = (String)messages.get(i);
//                i++ ;
//                lrcView.setText(message);
//                lrcView.invalidate();
//                songtime = (Long)times.get(ss-1);
//
//            }
//            if(offset >= nextTimeMill){
//                lrcView.setText(message);
//                lrcView.invalidate();
//
//                if(i < ss){
//                    message = (String)messages.get(i);
//                    nextTimeMill = (Long)times.get(i);
//                    i++;
//                }
//
//
//            }
//            if (currentTimeMill <= songtime){
//                //更新进度条
////                int bar = (int)Math.floor(currentTimeMill*10000/songtime);
////                seekBar.setProgress(bar);
//
//                currentTimeMill = currentTimeMill + 10;
//                handler.postDelayed(updateTimeCallback, 10);
//            }
//        }
//    }

}
