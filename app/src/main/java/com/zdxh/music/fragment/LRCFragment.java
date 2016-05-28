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
import android.widget.SeekBar;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.mp3.LrcView;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.MediaUtil;

import java.util.Iterator;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 听歌页面
 */
public class LRCFragment extends Fragment{
    private View view;
    private ReceiverPlayReceiver receiverPlayReceiver;
    private CollectionReceiver collectionReceiver;
    private ImageButton btnPlay,btnLove;
    private TextView tvSongName,tvSingerName;
    private String[] data;
    private ImageView album;
    public static LrcView lrcView; // 自定义歌词视图
    public static TextView mp3TitleTextView = null;
    public static SeekBar sbMusic;
    public static TextView finalTime;
    public static TextView currentTime;
    private int currentTimeMill = 0;
    private EntityBean.DataBean collectionDataBean = new EntityBean.DataBean();
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
        sbMusic = (SeekBar) view.findViewById(R.id.sbMusic);
        sbMusic.setOnSeekBarChangeListener(new SeekbarChangeListener());
        finalTime = (TextView) view.findViewById(R.id.finalTime);
        currentTime = (TextView) view.findViewById(R.id.currentTime);
        return view;
    }

    //更新seekBar
    private Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            //获得歌曲现在播放位置并设置成播放进度条的值
            currentTimeMill = MediaService.player.getCurrentPosition();
            sbMusic.setProgress(currentTimeMill);
            //每次延迟100毫秒再启动线程
            handler.postDelayed(updateThread, 100);
        }
    };
    //更新时间
    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            currentTimeMill = MediaService.player.getCurrentPosition();
            String time = MediaUtil.formatTime(currentTimeMill);
            currentTime.setText(time);
            handler.postDelayed(updateTime,1000);
        }
    };
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

            sbMusic.setVisibility(View.VISIBLE);
            currentTime.setVisibility(View.VISIBLE);
            finalTime.setVisibility(View.VISIBLE);

            data = SearchListAty.returnData();

            if (MediaService.RECEIVERPLAY.equals(intent.getAction())&&MediaService.isFinish==false){
                sbMusic.setMax(MediaService.player.getDuration()); //获得歌曲的长度并设置成播放进度条的最大值
                handler.post(updateThread);
                handler.post(updateTime);
                //从活动中获取相应Fragment的实例 更改button 的样式
                btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                MusicApplication.isPause = false;
                btnPlay.setOnClickListener(this);
                btnLove.setOnClickListener(this);

                if (MusicApplication.isFromSearchListAty == true){
                    tvSingerName.setText(data[0]);
                    tvSongName.setText(data[1]);
                    //设置歌曲时间
                    finalTime.setText(data[3]);
                }else {
                    if (MusicApplication.data[0]!=null){
                        tvSingerName.setText(MusicApplication.data[0]);
                        tvSongName.setText(MusicApplication.data[1]);
                        finalTime.setText(MusicApplication.data[3]);
                    }
                }

                String singerName = tvSingerName.getText().toString();
                MusicDB musicDB = MusicDB.getInstance(getContext());
                if (musicDB.isExistsImage(singerName)){
                    //从数据库中加载图片
                    album.setImageBitmap(musicDB.loadImage(singerName));
                }else {
                    album.setImageResource(R.drawable.music_icon);
                }


            }else {
                //取消线程
                handler.removeCallbacks(updateThread);
                handler.removeCallbacks(updateTime);
                btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                MediaService.isFinish = false;
                MusicApplication.isPause = true;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnPlay:
                    if (MusicApplication.isPause == false){
                        handler.removeCallbacks(updateThread);
                        handler.removeCallbacks(updateTime);
                        btnPlay.setImageResource(R.drawable.ic_play_arrow_white_18dp);
                        MusicApplication.isPause = true;

                        Intent intentPause = new Intent(getActivity(),MediaService.class);
                        intentPause.putExtra("songUrl",data[2]);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",SearchListAty.returnDataBean());
                        intentPause.putExtras(bundle);
                        intentPause.setAction(MediaService.PAUSE);
                        getActivity().startService(intentPause);

                    }else if (MusicApplication.isPause == true){
                        handler.post(updateThread); //开启线程
                        handler.post(updateTime);
                        btnPlay.setImageResource(R.drawable.ic_pause_white_18dp);
                        MusicApplication.isPause = false;
                        Intent intentPlay = new Intent(getActivity(),MediaService.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("databean",SearchListAty.returnDataBean());
                        intentPlay.putExtras(bundle);
                        intentPlay.putExtra("songUrl",data[2]);
                        intentPlay.setAction(MediaService.RESUME);
                        getActivity().startService(intentPlay);
                    }
                    break;
                case R.id.btnLove:
                    if (MusicApplication.isCollection == false){
                        btnLove.setImageResource(R.drawable.ic_favorite_red_500_18dp);
                        MusicApplication.isCollection = true;
                        //添加到收藏列表
                        if (MusicApplication.isFromSearchListAty){
                            if (tvSingerName.getText().toString().equals(SearchListAty.returnDataBean().getSinger_name())){
                                Log.d("TAG","11111111111");
                                MusicApplication.collections.add(SearchListAty.returnDataBean());
                            }
                        }else {
                            Log.d("TAG","2222222222");

                            collectionDataBean.setSinger_name(tvSingerName.getText().toString());
                            collectionDataBean.setSong_name(tvSongName.getText().toString());
                            MusicApplication.collections.add(collectionDataBean);
                        }


                    }else {
                        btnLove.setImageResource(R.drawable.ic_favorite_border_red_500_18dp);
                        MusicApplication.isCollection = false;
                        Iterator<EntityBean.DataBean> iterator = MusicApplication.collections.iterator();
                        if (iterator.hasNext()){
                            if (MusicApplication.isFromSearchListAty){
                                if (iterator.next() == SearchListAty.returnDataBean()){
                                    iterator.remove();
                                }
                            }else if (iterator.next() == collectionDataBean){
                                iterator.remove();
                            }

                        }
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

    private class SeekbarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //滑块滑动时调用的
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser == true){
                MediaService.player.seekTo(progress);
            }
        }
        //滑动开始滑动时调用的
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        //滑动停止时调用的
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

}
