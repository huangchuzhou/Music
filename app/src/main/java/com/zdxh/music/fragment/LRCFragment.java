package com.zdxh.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.service.MediaService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 听歌页面
 */
public class LRCFragment extends Fragment {
    private View view;
    private LocalReceiver localReceiver;
    private ImageButton btnPlay;
    private TextView tvSongName,tvSingerName;
    private String[] data;
    private ImageView album;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取广播的实例

        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaService.RECEIVERPLAY);
        localReceiver = new LocalReceiver();
        getActivity().registerReceiver(localReceiver,filter);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        data = SearchListAty.returnData();
        view = inflater.inflate(R.layout.lrc_fragment_layout,container,false);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        tvSingerName = (TextView) view.findViewById(R.id.tvSingerName);
        tvSongName = (TextView) view.findViewById(R.id.tvSongName);
        album = (ImageView) view.findViewById(R.id.album);
        return view;
    }

    class LocalReceiver extends BroadcastReceiver implements View.OnClickListener {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MediaService.RECEIVERPLAY.equals(intent.getAction())){
                Log.d("TAG",intent.getAction());
                //从活动中获取相应Fragment的实例 更改button 的样式
                btnPlay.setImageResource(R.drawable.icon_pause_normal);
                SearchListAty.isPause = false;
                btnPlay.setOnClickListener(this);
                tvSingerName.setText(data[0]);
                Log.d("TAG",tvSingerName.toString());
                tvSongName.setText(data[1]);

                //从数据库中加载图片
                String imageUrl = "http://lp.music.ttpod.com/pic/down?artist="+encodeName(data[3]);


                if (MusicDB.musicDB.loadImage(imageUrl)!=null){

                    album.setImageBitmap(MusicDB.musicDB.loadImage(imageUrl));

                }
            }
        }

        @Override
        public void onClick(View v) {

            if (SearchListAty.isPause == false){
                btnPlay.setImageResource(R.drawable.icon_play_normal);
                SearchListAty.isPause = true;

                Intent intentPause = new Intent(getActivity(),MediaService.class);
                intentPause.putExtra("songUrl",data[2]);

                intentPause.setAction(MediaService.PAUSE);
                getActivity().startService(intentPause);

            }else if (SearchListAty.isPause == true){
                btnPlay.setImageResource(R.drawable.icon_pause_normal);
                SearchListAty.isPause = false;

                Intent intentPlay = new Intent(getActivity(),MediaService.class);
                intentPlay.putExtra("songUrl",data[2]);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intentPlay.setAction(MediaService.PLAY);
                getActivity().startService(intentPlay);
            }
        }
    }

    //动态取消注册
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(localReceiver);
    }

    public String encodeName(String name) {

        String n = null;
        try {
            n = URLEncoder.encode(name, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return n;
    }
}
