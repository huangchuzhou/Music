package com.zdxh.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.fragment.LRCFragment;
import com.zdxh.music.fragment.MainFragment;
import com.zdxh.music.fragment.SearchFragment;
import com.zdxh.music.util.DrawerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    //设置tabLayout的每一个tab的标题
    private String[] mTitles;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    //抽屉菜单的listView adapter 根视图 draw_layout;
    private ListView left_drawer;
    private DrawerAdapter drawerAdapter;
    private String[] mDrawerItemText;
    private DrawerLayout draw_layout;
    private Fragment searchFragment;
    //设置每个tab所对应的Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitles = getResources().getStringArray(R.array.main_panel_title);
        mDrawerItemText = getResources().getStringArray(R.array.drawer_item);
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

        //初始化根视图 draw_layout 抽屉菜单区域的 lisView 以及设置与之对应的 adapter
        draw_layout = (DrawerLayout) findViewById(R.id.draw_layout);
        left_drawer = (ListView) findViewById(R.id.left_drawer);
        drawerAdapter = new DrawerAdapter(this,R.layout.drawer_list_item,mDrawerItemText);
        left_drawer.setAdapter(drawerAdapter);

        //left_drawer的列表点击事件
        left_drawer.setOnItemClickListener(this);


    }
    //处理left_drawer的列表点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        draw_layout.closeDrawer(left_drawer);

        switch (mDrawerItemText[position]){
            case "设置":
                Intent intent = new Intent(MainActivity.this,SetAty.class);
                startActivity(intent);
            break;
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
