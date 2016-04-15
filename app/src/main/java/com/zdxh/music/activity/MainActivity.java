package com.zdxh.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zdxh.music.R;
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

    }
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
