package com.zdxh.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.bean.ImageBean;
import com.zdxh.music.db.MusicDB;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.EnjoyAdapter;
import com.zdxh.music.util.ImageBeanCallBackListener;
import com.zdxh.music.util.Search;
import com.zdxh.music.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 发现页面
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private ListView enjoyList;
    private EnjoyAdapter adapter;
    private EnjoyReceiver mEnjoyReceiver;
    private ViewPager vp;
    private List<View> views;
    private ViewPagerAdapter vpAdapter;
    private ImageView[] dots; //声明导航点
    private int[] ids={R.id.iv1,R.id.iv2,R.id.iv3,R.id.iv4,R.id.iv5};//导航点对应的图标
    private ImageView ivAlbum;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MediaService.ENJOYPLAY);
        mEnjoyReceiver = new EnjoyReceiver();
        getActivity().registerReceiver(mEnjoyReceiver,filter);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_fragment_layout,container,false);
        enjoyList = (ListView) view.findViewById(R.id.enjoyList);


        vp = (ViewPager) view.findViewById(R.id.vp);
        views = new ArrayList<>();
        initViews();
        dots = new ImageView[views.size()];
        for (int i=0;i<views.size();i++){
            dots[i] = (ImageView)view.findViewById(ids[i]);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<EntityBean.DataBean> dataBeanArrayList = new ArrayList<>();
        //只取前3个数据作为最近在听列表的三个选项
        if (MusicApplication.getEnjoy().size()!=0){

            if (MusicApplication.getEnjoy().size()>3){
                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1),MusicApplication.getEnjoy().get(2)};
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(2));
                initSingerView(dataBeen);
            }else if (MusicApplication.getEnjoy().size()==3){
                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1),MusicApplication.getEnjoy().get(2)};
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(2));
                initSingerView(dataBeen);
            }else if(MusicApplication.getEnjoy().size()==2){
                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1)};
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
                initSingerView(dataBeen);
            }else if (MusicApplication.getEnjoy().size()==1){
                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0)};
                dataBeanArrayList.add(dataBeen[0]);
                initSingerView(dataBeen);
                Log.d("TAG","12333333333333");
            }
        }
        adapter = new EnjoyAdapter(getActivity(),R.layout.main_fragment_enjoy_item, dataBeanArrayList);

        enjoyList.setAdapter(adapter);

    }

    public void initSingerView(EntityBean.DataBean[] dataBeen){
        Log.d("TAG","123333333333334444444444444444444");
        for (int i=0;i<dataBeen.length;i++){
            Search mSearch = new Search(dataBeen[i].getSinger_name());
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View mView = inflater.inflate(R.layout.main_fragment_enjoy_item,null);
            ivAlbum = (ImageView) mView.findViewById(R.id.ivAlbum);

            mSearch.getImageBeanData(getActivity(), new ImageBeanCallBackListener() {
                @Override
                public void info(String imageUrl,String singerName,ImageBean mImageBean) {
                    if (MusicDB.musicDB.isExistsImage(imageUrl)){
                        Log.d("TAG",imageUrl);
                        ivAlbum.setImageBitmap(MusicDB.musicDB.loadImage(imageUrl));

                    }
                }
            });
        }
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        views.add(inflater.inflate(R.layout.imageone,null));
        views.add(inflater.inflate(R.layout.imagetwo,null));
        views.add(inflater.inflate(R.layout.imagethree,null));
        views.add(inflater.inflate(R.layout.imagefour,null));
        views.add(inflater.inflate(R.layout.imagefive,null));
        vpAdapter = new ViewPagerAdapter(getActivity(),views);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mEnjoyReceiver);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0;i<views.size();i++){
            if (position == i){
                dots[i].setImageResource(R.drawable.ic_fiber_manual_record_red_700_18dp);
            }else {
                dots[i].setImageResource(R.drawable.ic_fiber_manual_record_white_18dp);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class EnjoyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MediaService.ENJOYPLAY)){
                adapter.notifyDataSetChanged();
            }
        }
    }
}
