package com.zdxh.music.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.zdxh.music.bean.EntityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/5.
 */
public class MusicApplication extends Application {
    public static ArrayList<EntityBean.DataBean> enjoy;
    public static RequestQueue mRequestQueue;
    public static List<EntityBean.DataBean> dataBeensCollection;
    @Override
    public void onCreate() {
        super.onCreate();
        enjoy = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        dataBeensCollection = new ArrayList<>();
    }

    public static ArrayList<EntityBean.DataBean> getEnjoy(){
        return enjoy;
    }

    public static RequestQueue getRequestQueue(){

        return mRequestQueue;
    }

    public static List<EntityBean.DataBean> getDataBeensCollection(){
        return dataBeensCollection;
    }
}
