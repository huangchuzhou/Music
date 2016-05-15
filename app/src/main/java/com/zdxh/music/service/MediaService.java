package com.zdxh.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.fragment.LRCFragment;
import com.zdxh.music.mp3.LrcProcess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/17.
 */
public class MediaService extends Service {
    //标记是否播放
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String RESUME = "RESUME";
    public static boolean isPlay = false;
    public static boolean isFinish = false;
    //广播接收action
    public static final String RECEIVERPLAY = "RECEIVERPLAY"; //接收播放
    public static final String ENJOYPLAY = "ENJOYPLAY";  //MainFragment的最近播放列表的标志
    public static final String RECEIVERFINISH = "RECEIVERFINISH";//完成播放
    public static final String COLLECTION = "COLLECTION";
    private MediaPlayer player = new MediaPlayer();

    private LrcProcess mLrcProcess; //歌词处理
    private ArrayList<String> lrcList = new ArrayList<>(); //存放歌词列表对象
    private ArrayList<Long> timeList = new ArrayList<>(); //存放歌词时间对象
    private int index = 0;          //歌词检索值
    public static final String SHOW_LRC = "SHOW_LRC"; //通知显示歌词
    private int currentTime;    //当前播放进度
    private int duration;       //播放长度
    public static final String MUSIC_CURRENT = "MUSIC_CURRENT";	//当前音乐播放时间更新动作
    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (player != null) {
                    currentTime = player.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(MUSIC_CURRENT);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        if (intent !=null){
            String songUrl = intent.getStringExtra("songUrl");
            Bundle bundle = intent.getExtras();
            EntityBean.DataBean dataBean = (EntityBean.DataBean) bundle.getSerializable("databean");
            //判断songUrl是本地文件还是网络文件
            if (songUrl.endsWith("mp3")){
                //本地播放音乐
                localPlay(intent,songUrl,dataBean);
            }else {
                remotePlay(intent,songUrl,dataBean);
            }
        }
        //暂停音乐
        pause(intent);
        //完成音乐播放
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                player.release();

                //发送广播给SearchListAty 更改button样式
                Intent intentFinish = new Intent();
                intentFinish.setAction(RECEIVERFINISH);
                sendBroadcast(intentFinish);

                //发送广播给LRCFragment 更改button样式
                Intent intentPlay = new Intent();
                intentPlay.setAction(RECEIVERPLAY);
                isFinish = true;
                sendBroadcast(intentPlay);

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    private void localPlay(Intent intent, String songUrl, EntityBean.DataBean dataBean) {
        //传过来的action是播放play
        if (PLAY.equals(intent.getAction())) {
            initLrc(dataBean);
            if (player.isPlaying()) {
                player.reset();
            }
            try {
                player.setDataSource(songUrl);
                player.prepare();
                player.start();
                isPlay = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //发送广播
            Intent IntentPlay = new Intent(MediaService.RECEIVERPLAY);
            sendBroadcast(IntentPlay);
        }
    }


    //暂停音乐
    private void pause(Intent intent) {
        //传过来的action是暂停pause
        if (PAUSE.equals(intent.getAction())){

            if (player != null && player.isPlaying()){

                player.pause();
                Intent intentPause = new Intent();
                intentPause.putExtra("pause",PAUSE);
                sendBroadcast(intentPause);
            }
        }else if (RESUME.equals(intent.getAction()) && player!=null){
            player.start();
            isPlay = true;
        }
    }

    //播放音乐
    private void remotePlay(Intent intent, String songUrl, EntityBean.DataBean dataBean) {
        //传过来的action是播放play
        if (PLAY.equals(intent.getAction())) {
            initLrc(dataBean);
            if (player.isPlaying()) {
                player.reset();
            }
            player = MediaPlayer.create(this, Uri.parse(songUrl));
            player.start();
            isPlay = true;

            //发送广播
            Intent IntentPlay = new Intent(MediaService.RECEIVERPLAY);
            sendBroadcast(IntentPlay);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null){
            player.stop();
            player.release();
            player = null;
        }
    }

    /**
     * 初始化歌词配置
    */
    public void initLrc(EntityBean.DataBean dataBean){

        mLrcProcess = new LrcProcess();
        int lrcId;
        //读取歌词文件

        //获取lrc文件
        lrcId = MusicDB.musicDB.getLrcId(dataBean.getSong_name(),dataBean.getSinger_name());

        if (lrcId != -1){
            LRCFragment.lrcView.setVisibility(View.VISIBLE);
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +"/downloads/lrc/"+lrcId+".lrc");
                ArrayList<String> mp3Titles = mLrcProcess.getMp3Titles();
                mLrcProcess.process(inputStream);
                lrcList = mLrcProcess.getLrcList();
                timeList = mLrcProcess.getTimeList();
                LRCFragment.lrcView.setmLrcList(lrcList);
                LRCFragment.mp3TitleTextView.setText("歌名："+mp3Titles.get(0)+"\n"+"歌手："+mp3Titles.get(1)+"\n"+"专辑："+mp3Titles.get(2));
                //切换带动画显示歌词
                LRCFragment.lrcView.setAnimation(AnimationUtils.loadAnimation(MediaService.this, R.anim.alpha_z));
                handler.post(mRunnable);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            LRCFragment.lrcView.setVisibility(View.INVISIBLE);
            LRCFragment.mp3TitleTextView.setText("没有找到相关的歌词文件");
        }

    }
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            LRCFragment.lrcView.setIndex(lrcIndex());
            LRCFragment.lrcView.invalidate();
            handler.postDelayed(mRunnable, 100);
        }
    };


    /**
     * 根据时间获取歌词显示的索引值
     * @return
     */
    public int lrcIndex() {
        if(player.isPlaying()) {
            currentTime = player.getCurrentPosition();
            duration = player.getDuration();
        }
        if(currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < timeList.get(i) && i == 0) {
                        index = i;
                    }
                    if (currentTime > timeList.get(i)
                            && currentTime < timeList.get(i + 1)) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > timeList.get(i)) {
                    index = i;
                }
            }
        }
        return index;
    }
}


