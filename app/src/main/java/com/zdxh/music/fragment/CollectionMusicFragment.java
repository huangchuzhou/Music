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
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.util.CollectionMusicAdapter;

import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/5/13.
 */
public class CollectionMusicFragment extends Fragment implements View.OnClickListener {
    private ListView collectionMusicListView;
    private ImageButton btnBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_music,container,false);
        collectionMusicListView = (ListView) view.findViewById(R.id.collectionMusicListView);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        //将set集合转成list集合
        ArrayList<EntityBean.DataBean> dataBeens = new ArrayList<>(MusicApplication.collections);
        CollectionMusicAdapter adapter = new CollectionMusicAdapter(getContext(),R.layout.collection_music_item, dataBeens);
        collectionMusicListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}
