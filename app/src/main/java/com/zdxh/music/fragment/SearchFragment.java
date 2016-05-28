package com.zdxh.music.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.db.DBHelper;
import com.zdxh.music.flowlayout.FlowLayout;
import com.zdxh.music.mp3.Mp3Info;
import com.zdxh.music.util.DataCallBackListener;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 搜索页面
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private FlowLayout mFlowLayout;
    private EditText etSearch;
    private ImageButton btnSearch;
    private String[] hotLabels = {"洋葱","那些年","See you Again","Every Time","小幸运","always"};
    private Button btnLocalMusic, btnDownloadMusic, btnCollectMusic, btnRecentlyMusic;
    private File file; //文件下载的保存路径

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_layout,container,false);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        btnLocalMusic = (Button) view.findViewById(R.id.btnLocalMusic);
        btnDownloadMusic = (Button) view.findViewById(R.id.btnDownloadMusic);
        btnCollectMusic = (Button) view.findViewById(R.id.btnCollectMusic);
        btnRecentlyMusic = (Button) view.findViewById(R.id.btnRecentlyMusic);

        btnLocalMusic.setOnClickListener(this);
        btnDownloadMusic.setOnClickListener(this);
        btnRecentlyMusic.setOnClickListener(this);
        btnCollectMusic.setOnClickListener(this);
        init();
        return view;
    }




    //初始化热门标签视图
    private void init() {
        for (String hotLabel:hotLabels){
            addHotLable(hotLabel, new DataCallBackListener() {
                @Override
                public void onFinish(String hotLabelText) {
                    etSearch.setText(hotLabelText);
                    //把光标移到最后
                    etSearch.setSelection(etSearch.getText().length());
                }
            });
        }
    }

    private void addHotLable(String hotLabel,final DataCallBackListener listener) {
        final TextView tvHotLabel = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tv_hot_label, mFlowLayout, false);
        tvHotLabel.setText(hotLabel);
        tvHotLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinish(tvHotLabel.getText().toString());
            }
        });

        mFlowLayout.addView(tvHotLabel);
    }

    @Override
    public void onClick(View v) {
        ArrayList<Mp3Info> mp3Infos;
        switch (v.getId()){
            case R.id.btnSearch:
                //此处的fragment与另一个activity进行通信
                Intent intent = new Intent(getActivity(), SearchListAty.class);
                intent.putExtra("searchName", etSearch.getText().toString());
                if (TextUtils.isEmpty(etSearch.getText().toString())){
                    Toast.makeText(getActivity(),"输入不能为空",Toast.LENGTH_SHORT).show();
                }else{

                    getActivity().startActivity(intent);
                }
                break;
            case R.id.btnCollectMusic:
                loadCollectionMusicFragment();
                break;
            case R.id.btnDownloadMusic:
                mp3Infos = localMusic();
                loadDownloadMusicFragment(mp3Infos);
                break;
            case R.id.btnLocalMusic:
                mp3Infos = localMusic();
                loadLocalMusicFragment(mp3Infos);
                break;
            case R.id.btnRecentlyMusic:
                loadRecentlyPlayMusicFragment();
                break;
        }

    }


    //获取SDcard中的MP3文件  本地音乐
    private ArrayList<Mp3Info> localMusic() {
        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        Cursor cursor = null;
        DBHelper mDBHelper = new DBHelper(getActivity());
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/");
        if (file.exists()){
            //获取该路径下的所有文件
            File[] files = file.listFiles();
            for (int i=0;i<files.length;i++){

                if (files[i].getName().endsWith("mp3")){
                    Mp3Info mp3Info = new Mp3Info();
                    String songId = files[i].getName();
                    String[] id = songId.split("\\."); // 分隔字符串
                    mp3Info.setSong_id(id[0]);
                    Log.d("TAG","id="+id[0]+"-------------");
                    //从数据库中查找获取MP3Info的其他变量的值

                    cursor = db.rawQuery("select * from entity where song_id = ?",new String[]{id[0]});
                    if (cursor.moveToNext()){
                        mp3Info.setSize(cursor.getString(cursor.getColumnIndex("size")));
                        mp3Info.setSong_Name(cursor.getString(cursor.getColumnIndex("song_name")));
                        mp3Info.setSinger_name(cursor.getString(cursor.getColumnIndex("singer_name")));
                        mp3Info.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
                        mp3Infos.add(mp3Info);
                    }
                    Log.d("TAG","mp3Infos"+mp3Infos.toString());

                    cursor.close(); //用完后必须关闭

                }
            }
            db.close();
        }

        return mp3Infos;
    }

    //动态加载LocalMusicFragment
    private void loadLocalMusicFragment(ArrayList<Mp3Info> mp3Infos){
        LocalMusicFragment mLocalMusicFragment = new LocalMusicFragment();
        Bundle bundle = new Bundle();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .add(R.id.main_content,mLocalMusicFragment)
                .commit();
        bundle.putParcelableArrayList("mp3Infos", mp3Infos);
        mLocalMusicFragment.setArguments(bundle);
    }

    //动态加载DownloadMusicFragment
    private void loadDownloadMusicFragment(ArrayList<Mp3Info> mp3Infos){
        DownloadMusicFragment mDownloadMusicFragment= new DownloadMusicFragment();
        Bundle bundle = new Bundle();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .add(R.id.main_content,mDownloadMusicFragment)
                .commit();
        bundle.putParcelableArrayList("mp3Infos", mp3Infos);
        bundle.putString("address",file.toString());
        mDownloadMusicFragment.setArguments(bundle);
    }

    //动态加载CollectionMusicFragment
    private void loadCollectionMusicFragment(){
        CollectionMusicFragment mCollectionMusicFragment= new CollectionMusicFragment();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .add(R.id.main_content,mCollectionMusicFragment)
                .commit();
    }

    //动态加载RecentlyPlayMusicFragment
    private void loadRecentlyPlayMusicFragment(){
        RecentlyPlayMusicFragment mRecentlyPlayMusicFragment = new RecentlyPlayMusicFragment();
        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .add(R.id.main_content,mRecentlyPlayMusicFragment)
                .commit();
    }
}
