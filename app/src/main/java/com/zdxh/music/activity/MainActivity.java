package com.zdxh.music.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zdxh.music.R;
import com.zdxh.music.flowlayout.InfoWindow;
import com.zdxh.music.fragment.LRCFragment;
import com.zdxh.music.fragment.MainFragment;
import com.zdxh.music.fragment.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    //设置tabLayout的每一个tab的标题
    private String[] mTitles;
    //设置每个viewpager的fragment
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //声明SlidingMenu
    private SlidingMenu mSlidingMenu;

    //侧滑菜单的启动按钮
    private ImageButton btnMenu;


    private Fragment searchFragment;
    //设置每个tab所对应的Fragment

    //点击弹出窗口的按钮
    private ImageButton btnInfo;

    //自定义弹出窗口类
    private InfoWindow mInfoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitles = getResources().getStringArray(R.array.main_panel_title);

        for (int i = 0; i<mTitles.length; i++){

            switch (mTitles[i]){
                case "搜索":
                    searchFragment = new SearchFragment();
                    mFragments.add(searchFragment);
                    break;
                case "发现":
                    Fragment mainFragment = new MainFragment();
                    mFragments.add(mainFragment);
                    break;
                case "听歌":
                    Fragment lrcFragment = new LRCFragment();
                    mFragments.add(lrcFragment);
                    break;
            }
        }
        //初始化viewpager adapter tabLayout
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mTitles,mFragments);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //缓存当前界面每一侧的界面数
        mViewPager.setOffscreenPageLimit(2);
        //指定当前页为进入程序所展示的页面
        mViewPager.setCurrentItem(1);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        //初始化SlidingMenu及定义其属性
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        mSlidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.sildingmenu);

        btnMenu = (ImageButton) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);


        btnInfo = (ImageButton) findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                mInfoWindow = new InfoWindow(MainActivity.this, itemsOnClick);
                //显示窗口  设置layout在PopupWindow中显示的位置
                mInfoWindow.showAtLocation(MainActivity.this.btnInfo, Gravity.RIGHT, 0, -425);
            }
        });

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            mInfoWindow.dismiss();
            switch (v.getId()) {
                case R.id.about:
                    //在这里设置为弹出一个对话框
                    AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
                    alert.setTitle("关于我们");
                    alert.setMessage("我们今晚来一发");
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "( ^_^ )/~~拜拜", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"爱我别走，如果你说你不爱我。。。",Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "小婊砸", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this,"得不到的永远在骚动\n被偏爱的有恃无恐\n감사합니다.",Toast.LENGTH_LONG).show();
                        }
                    });
                    alert.show();
                    break;

            }


        }

    };
    //处理启动菜单按钮的回调事件
    @Override
    public void onClick(View v) {
        mSlidingMenu.toggle(true);  //slidingMenu切换菜单呈现一个切换效果
    }

    //通过物理菜单键打开SlidingMenu菜单
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MENU:
                mSlidingMenu.toggle(true);  //slidingMenu切换菜单呈现一个切换效果
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    //通过物理返回键来关闭SlidingMenu菜单
    @Override
    public void onBackPressed() {
        if (mSlidingMenu.isMenuShowing()){
            mSlidingMenu.showContent();
        }else {
            super.onBackPressed();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String[] mTitles;
        private ArrayList<Fragment> fragments;
        public SectionsPagerAdapter(FragmentManager fm, String[] mTitles, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.mTitles = mTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {

            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }


}
