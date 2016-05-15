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
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.util.RecentlyPlayMusicAdapter;

/**
 * Created by huangchuzhou on 2016/5/13.
 */
public class RecentlyPlayMusicFragment extends Fragment implements View.OnClickListener {
    private ListView recentlyPlayMusicListView;
    private ImageButton btnBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recently_play_music,container,false);
        recentlyPlayMusicListView = (ListView) view.findViewById(R.id.recentlyPlayMusicListView);
        RecentlyPlayMusicAdapter adapter = new RecentlyPlayMusicAdapter(getContext(),R.layout.recently_play_music_item, MusicApplication.dataBeans);
        recentlyPlayMusicListView.setAdapter(adapter);

        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}
