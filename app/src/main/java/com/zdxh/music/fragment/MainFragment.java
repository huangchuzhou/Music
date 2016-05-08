package com.zdxh.music.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.service.MediaService;
import com.zdxh.music.util.EnjoyAdapter;
import com.zdxh.music.util.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 发现页面
 */
public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener, View.OnClickListener {
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
    //返回到LRCFragment的数据有数组包装（SingerName SongName SongUrl）
    private static String[] data = new String[4];
    //声明SlidingMenu
    private SlidingMenu mSlidingMenu;
    private LinearLayout more;
    private ImageButton back;
    private TextView t;
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
        more = (LinearLayout) view.findViewById(R.id.more);
        more.setOnClickListener(this);
        vp = (ViewPager) view.findViewById(R.id.vp);

        View mView = inflater.inflate(R.layout.more_content,null);
        back = (ImageButton) mView.findViewById(R.id.back);
        t = (TextView) mView.findViewById(R.id.t);
        views = new ArrayList<>();
        initViews();
        dots = new ImageView[views.size()];
        for (int i=0;i<views.size();i++){
            dots[i] = (ImageView)view.findViewById(ids[i]);
        }

        //初始化SlidingMenu及定义其属性
        mSlidingMenu = new SlidingMenu(getActivity());
        mSlidingMenu.setMode(SlidingMenu.RIGHT);
        mSlidingMenu.attachToActivity(getActivity(),SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.more_content);

        return view;
    }

    @Override
    public void onResume() {
        isPlaying = false;
        super.onResume();
        dataBeanArrayList = new ArrayList<>();
        //只取前3个数据作为最近在听列表的三个选项
        if (MusicApplication.getEnjoy().size()!=0){

            if (MusicApplication.getEnjoy().size()>3){
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(2));
//                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1),MusicApplication.getEnjoy().get(2)};
//                isEquals(dataBeen);
            }else if (MusicApplication.getEnjoy().size()==3){
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(2));
//                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1),MusicApplication.getEnjoy().get(2)};
//                isEquals(dataBeen);
            }else if(MusicApplication.getEnjoy().size()==2){
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(1));
//                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0),MusicApplication.getEnjoy().get(1)};
//                isEquals(dataBeen);
            }else if (MusicApplication.getEnjoy().size()==1){
                dataBeanArrayList.add(MusicApplication.getEnjoy().get(0));
//                EntityBean.DataBean[] dataBeen = {MusicApplication.getEnjoy().get(0)};
//                isEquals(dataBeen);
            }
        }
        adapter = new EnjoyAdapter(getActivity(),R.layout.main_fragment_enjoy_item, dataBeanArrayList);

        enjoyList.setAdapter(adapter);
        enjoyList.setOnItemClickListener(this);
        back.setOnClickListener(this);
        //处理Fragment的按返回键
        // 主界面获取焦点
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button
                    if (mSlidingMenu.isMenuShowing()){
                        mSlidingMenu.showContent();
                    }
                    return true;
                }
                return false;
            }
        });

    }

//    private boolean isEquals(EntityBean.DataBean[] dataBeen) {
//        if (dataBeen.length>=3){
//            if (dataBeen[0].equals(dataBeen[1])){
//
//            }
//        }
//    }


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
        isPlaying = true;
        EntityBean.DataBean mDatabean = dataBeanArrayList.get(position);
        List<EntityBean.DataBean.AuditionListBean> auditionListBeenList = mDatabean.getAudition_list();
        EntityBean.DataBean.AuditionListBean auditionListBean = auditionListBeenList.get(SearchListAty.songUrlLocation);
        String songUrl = auditionListBean.getUrl();

        //传递songUrl给MediaService
        Intent intent = new Intent(getActivity(), MediaService.class);
        intent.putExtra("songUrl", songUrl);
        intent.setAction(MediaService.PLAY);
        getActivity().startService(intent);

        data[0] = mDatabean.getSinger_name();  //data数组第一项保存SingerName
        data[1] = mDatabean.getSong_name();  //data数组第二项保存SongName
        data[2] = songUrl;   //data数组第三项保存songUrl

        Intent mIntent = new Intent();
        mIntent.setAction(MediaService.RECEIVERPLAY);

        getActivity().sendBroadcast(mIntent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                t.setText("12333");
                mSlidingMenu.toggle(true);  //slidingMenu切换菜单呈现一个切换效果
                break;
            case R.id.back:
                //slidingMenu的回退事件
                if (mSlidingMenu.isMenuShowing()){
                    mSlidingMenu.showContent();
                }
                break;
        }


    }


    class EnjoyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MediaService.ENJOYPLAY)){
                adapter.notifyDataSetChanged();
            }
        }
    }



    public static String[] returnData()
    {
        return data;
    }
}
