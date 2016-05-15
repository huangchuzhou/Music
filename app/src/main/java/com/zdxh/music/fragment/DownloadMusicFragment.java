package com.zdxh.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.mp3.Mp3Info;
import com.zdxh.music.util.DownloadMusicAdapter;

import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/5/13.
 */
public class DownloadMusicFragment extends Fragment implements View.OnClickListener {
    private ListView downloadMusicListView;
    private TextView tvAddress;
    private ImageButton btnBack;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_music,container,false);
        downloadMusicListView = (ListView) view.findViewById(R.id.downloadMusicListView);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        Bundle bundle = getArguments();
        ArrayList<Mp3Info> mp3Infos = bundle.getParcelableArrayList("mp3Infos");
        String address = bundle.getString("address");
        DownloadMusicAdapter adapter = new DownloadMusicAdapter(getContext(),R.layout.download_music_item,mp3Infos);
        downloadMusicListView.setAdapter(adapter);

        tvAddress.setText("文件的保存路径："+address);
        return view;
    }

    @Override
    public void onClick(View v) {
        getFragmentManager().popBackStack();
    }
}
