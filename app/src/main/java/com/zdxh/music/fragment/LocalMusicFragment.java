package com.zdxh.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.mp3.Mp3Info;
import com.zdxh.music.util.LocalMusicAdapter;

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
        ArrayList<Mp3Info> mp3Infos = bundle.getParcelableArrayList("mp3Infos");
        LocalMusicAdapter adapter = new LocalMusicAdapter(getActivity(),R.layout.local_music_item,mp3Infos);
        localMusicListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                getFragmentManager().popBackStack(); //点击按钮返回主界面
                break;
        }
    }

    //    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.local_music,container,false);
//        localMusicListView = (ListView) view.findViewById(R.id.localMusicListView);
////        Bundle bundle = getArguments();
////        if (bundle!=null){
////            mp3Infos = (List<Mp3Info>) bundle.getSerializable("mp3Infos");
////            LocalMusicAdapter adapter = new LocalMusicAdapter(getContext(),R.layout.local_music_item,mp3Infos);
////            localMusicListView.setAdapter(adapter);
////        }
//        return view;
//    }
}
