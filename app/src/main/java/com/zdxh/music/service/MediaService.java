package com.zdxh.music.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by huangchuzhou on 2016/4/17.
 */
public class MediaService extends Service {
    //标记是否播放
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static boolean isFinish = false;
    //广播接收action
    public static final String RECEIVERPLAY = "RECEIVERPLAY"; //接收播放
    public static final String ENJOYPLAY = "ENJOYPLAY";  //爱心
    public static final String RECEIVERFINISH = "RECEIVERFINISH";//完成播放
    private MediaPlayer player = new MediaPlayer();



    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        String songUrl = intent.getStringExtra("songUrl");

        //传过来的action是播放play
        if (PLAY.equals(intent.getAction())){
            if (player.isPlaying()){
                player.reset();
            }
            player=MediaPlayer.create(this,Uri.parse(songUrl));
            player.start();

            //发送广播
            Intent IntentPlay = new Intent(MediaService.RECEIVERPLAY);
            sendBroadcast(IntentPlay);

            //传过来的action是播放pause
        }else if (PAUSE.equals(intent.getAction())){

            if (player != null && player.isPlaying()){

                player.pause();
                Intent intentPause = new Intent();
                intent.putExtra("pause",PAUSE);
                sendBroadcast(intentPause);
            }
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.release();
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


}


