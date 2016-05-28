package com.zdxh.music.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.zdxh.music.application.MusicApplication;

/**
 * Created by huangchuzhou on 2016/5/10.
 * 监听网络变化
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG","action"+intent.getAction());
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobileInfo.isConnected() && !wifiInfo.isConnected()){
            MusicApplication.isNetworkAvailable = false;
            Log.d("TAG","当前网络不可用");
        }else {
            MusicApplication.isNetworkAvailable = true;
            Log.d("TAG","当前网络可用");
        }

    }
}