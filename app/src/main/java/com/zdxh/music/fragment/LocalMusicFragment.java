package com.zdxh.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.mp3.Mp3Info;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.DownloadMp3;
import com.zdxh.music.util.LocalMusicAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/12.
 */
public class LocalMusicFragment extends Fragment implements View.OnClickListener {
    private ListView localMusicListView;
    private List<Mp3Info> mp3Infos;
    private ImageButton btnBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_music,container,false);
        localMusicListView = (ListView) view.findViewById(R.id.localMusicListView);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        Bundle bundle = getArguments();
        final ArrayList<Mp3Info> mp3Infos = bundle.getParcelableArrayList("mp3Infos");
        LocalMusicAdapter adapter = new LocalMusicAdapter(getActivity(),R.layout.local_music_item,mp3Infos);
        localMusicListView.setAdapter(adapter);
        localMusicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Mp3Info mp3Info = mp3Infos.get(position);
                String songId = mp3Info.getSong_id();
                File file = new File(DownloadMp3.DOWNLOAD_PATH,songId+".mp3");

                if(file.exists()){

                    MusicApplication.data[0] = mp3Info.getSinger_name();
                    MusicApplication.data[1] = mp3Info.getSong_Name();
                    MusicApplication.data[3] = mp3Info.getDuration();

                    //从本地获取Mp3文件
                    sendSongUrlFromLMFToService(mp3Info,file.getAbsolutePath());
                    Intent intent = new Intent();
                    intent.setAction(MediaService.RECEIVERPLAY);
                    getActivity().sendBroadcast(intent);
                }
                for (EntityBean.DataBean databean : MusicApplication.collections) {
                    if (databean.getSinger_name() == mp3Info.getSinger_name() && databean.getSong_name() == mp3Info.getSong_Name()){
                        MusicApplication.isCollection = true; //有被收藏
                        //发送广播
                        Intent intentOne = new Intent();
                        intentOne.setAction(MediaService.COLLECTION);
                        getActivity().sendBroadcast(intentOne);
                        break;
                    }else {
                        MusicApplication.isCollection = false; //没有被收藏
                        //发送广播
                        Intent intentTwo = new Intent();
                        intentTwo.setAction(MediaService.COLLECTION);
                        getActivity().sendBroadcast(intentTwo);
                    }
                }
            }
        });
        return view;
    }

    public void  sendSongUrlFromLMFToService(Mp3Info mp3Info,String songUrl){
        //传递songUrl给MediaService
        Intent intent = new Intent(getActivity(), MediaService.class);
        intent.putExtra("songUrl",songUrl);
        Bundle bundle = new Bundle();
        bundle.putSerializable("databean",null);
        bundle.putParcelable("mp3Info",mp3Info);
        intent.putExtras(bundle);
        intent.setAction(MusicApplication.PLAY);
//        isPause = false;
        getActivity().startService(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                getFragmentManager().popBackStack(); //点击按钮返回主界面
                break;
        }
    }

}
