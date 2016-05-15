package com.zdxh.music.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zdxh.music.application.MusicApplication;

/**
 * Created by huangchuzhou on 2016/5/10.
 * 监听网络变化
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobileInfo.isConnected() && !wifiInfo.isConnected()){
            MusicApplication.isNetworkAvailable = false;
        }else {
            MusicApplication.isNetworkAvailable = true;
        }

    }
}