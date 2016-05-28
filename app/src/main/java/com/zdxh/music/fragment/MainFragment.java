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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.DownloadMp3;
import com.zdxh.music.util.EnjoyAdapter;
import com.zdxh.music.util.ViewPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 发现页面
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener{
    private ListView enjoyList;
    private EnjoyAdapter adapter;
    private EnjoyReceiver mEnjoyReceiver;
    private ViewPager vp;
    private List<View> views;
    private ViewPagerAdapter vpAdapter;
    private ImageView[] dots; //声明导航点
    private int[] ids={R.id.iv1,R.id.iv2,R.id.iv3,R.id.iv4,R.id.iv5};//导航点对应的图标
    private ArrayList<EntityBean.DataBean> dataBeanArrayList;
    public static boolean isPlaying = false;

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
        isPlaying = false;
        super.onResume();
        dataBeanArrayList = new ArrayList<>();
        //只取前3个数据作为最近在听列表的三个选项
        if (MusicApplication.getEnjoy().size()!=0){
            ArrayList<EntityBean.DataBean> dataBeens = new ArrayList<>(MusicApplication.getEnjoy());
            if (MusicApplication.getEnjoy().size()>3){

                dataBeanArrayList.add(dataBeens.get(0));
                dataBeanArrayList.add(dataBeens.get(1));
                dataBeanArrayList.add(dataBeens.get(2));

            }else if (MusicApplication.getEnjoy().size()==3){
                dataBeanArrayList.add(dataBeens.get(0));
                dataBeanArrayList.add(dataBeens.get(1));
                dataBeanArrayList.add(dataBeens.get(2));

            }else if(MusicApplication.getEnjoy().size()==2){
                dataBeanArrayList.add(dataBeens.get(0));
                dataBeanArrayList.add(dataBeens.get(1));

            }else if (MusicApplication.getEnjoy().size()==1){
                dataBeanArrayList.add(dataBeens.get(0));

            }
        }
        adapter = new EnjoyAdapter(getActivity(),R.layout.main_fragment_enjoy_item, dataBeanArrayList);

        enjoyList.setAdapter(adapter);
        enjoyList.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicApplication.isFromSearchListAty = false;
        MusicApplication.isCollection = false;
        EntityBean.DataBean.AuditionListBean auditionListBean;
        String songUrl;
        isPlaying = true;
        MediaService.isPlay = true;
        EntityBean.DataBean mDatabean = dataBeanArrayList.get(position);
        Log.d("TAG",mDatabean.toString());
        List<EntityBean.DataBean.AuditionListBean> auditionListBeenList = mDatabean.getAudition_list();
        auditionListBean = auditionListBeenList.get(MusicApplication.songUrlLocation[position]);

        //先判断在内存card中是否有相应的歌曲
        File file = new File(DownloadMp3.DOWNLOAD_PATH,mDatabean.getSong_id()+".mp3");
        if (file.exists()){
            songUrl = file.getAbsolutePath();
        }else {
            songUrl = auditionListBean.getUrl();
        }


        //传递songUrl给MediaService
        Intent intent = new Intent(getActivity(), MediaService.class);
        intent.putExtra("songUrl", songUrl);
        Bundle bundle = new Bundle();
        bundle.putSerializable("databean",mDatabean);
        intent.putExtras(bundle);

        intent.setAction(MusicApplication.PLAY);
        getActivity().startService(intent);

        MusicApplication.data[0] = mDatabean.getSinger_name();  //data数组第一项保存SingerName
        MusicApplication.data[1] = mDatabean.getSong_name();  //data数组第二项保存SongName
        MusicApplication.data[2] = songUrl;   //data数组第三项保存songUrl
        MusicApplication.data[3] = auditionListBean.getDuration(); //data数组第四项保存时间
        for (EntityBean.DataBean databean : MusicApplication.collections) {
            if (databean == mDatabean){
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
        Intent mIntent = new Intent();
        mIntent.setAction(MediaService.RECEIVERPLAY);
        getActivity().sendBroadcast(mIntent);

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
