package com.zdxh.music.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zdxh.music.bean.EntityBean;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by huangchuzhou on 2016/5/5.
 */
public class MusicApplication extends Application {
//    public static ArrayList<EntityBean.DataBean> enjoy;   //最近播放列表的数据
    public static ArrayList<EntityBean.DataBean> enjoy;  //最近播放列表的数据(不可重复)
    public static RequestQueue mRequestQueue;
//    public static List<EntityBean.DataBean> dataBeensCollection;
    //标志此歌是否被收藏
    public static boolean isCollection = false;

    //收藏列表数据(不可以重复)
    public static HashSet<EntityBean.DataBean> collections = new HashSet<>();

    //此标志位用于获取searchListView item的点击位置，用于MainFragment的enjoyList item 对应的songUrl
    public static int[] songUrlLocation ={-1,-1,-1};

    public static int flag = -1; //标志添加到MainFragment的enjoyList的个数
    //返回到LRCFragment的数据有数组包装（SingerName SongName SongUrl）
    public static String[] data = new String[3];

    public static boolean isShowOther = false; //标志是否展示SearchListAty 的other页面

    public static boolean isNetworkAvailable = true; //监听网络是否可用

    public static ArrayList<EntityBean.DataBean> dataBeans = new ArrayList<>(); //保存最近播放的歌曲

    public static boolean isFromSearchListAty = false;  //标志数据来源
    @Override
    public void onCreate() {
        super.onCreate();
        enjoy = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        dataBeensCollection = new ArrayList<>();

    }

    //MainFragment界面列表的数据
    public static ArrayList<EntityBean.DataBean> getEnjoy(){
        return enjoy;
    }

    public static RequestQueue getRequestQueue(){

        return mRequestQueue;
    }

//    public static List<EntityBean.DataBean> getDataBeensCollection(){
//
//        return dataBeensCollection;
//    }




}
