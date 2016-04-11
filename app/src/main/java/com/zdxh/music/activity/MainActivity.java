package com.zdxh.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zdxh.music.R;
import com.zdxh.music.fragment.LRCFragment;
import com.zdxh.music.fragment.MainFragment;
import com.zdxh.music.fragment.SearchFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;
    private String[] mTitles = {"搜索","发现","听歌"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i<mTitles.length; i++){

            switch (mTitles[i]){
                case "搜索":
                    Fragment searchFragment = new SearchFragment();
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mTitles,mFragments);


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }





    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String[] mTitles;
        private ArrayList<Fragment> fragments;
        public SectionsPagerAdapter(FragmentManager fm,String[] mTitles,ArrayList<Fragment> fragments) {
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
