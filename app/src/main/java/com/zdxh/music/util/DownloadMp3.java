package com.zdxh.music.util;


import android.os.Environment;

import com.zdxh.music.bean.EntityBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by huangchuzhou on 2016/5/11.
 * 下载mp3文件到sdCard
 */
public class DownloadMp3 extends Thread {
    private EntityBean.DataBean.AuditionListBean auditionListBean = null;
    private EntityBean.DataBean dataBean = null;
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/downloads/";
    public DownloadMp3(EntityBean.DataBean dataBean,EntityBean.DataBean.AuditionListBean auditionListBean){
        this.auditionListBean = auditionListBean;
        this.dataBean = dataBean;
    }

    @Override
    public void run() {
        super.run();
        HttpURLConnection connection = null;
        try {

            //设置文件的写入位置
            File dir = new File(DOWNLOAD_PATH);
            if (!dir.exists()){
                dir.mkdir();
            }
            File file = new File(DOWNLOAD_PATH,dataBean.getSong_id()+".mp3");
            if (!file.exists()){
                file.createNewFile();
            }

            // 得到文件输出流
            URL url = new URL(auditionListBean.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            InputStream is = connection.getInputStream();
            writeToSDcard(is,file);


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connection.disconnect();
        }
    }

    private void writeToSDcard(InputStream is,File file) {
        OutputStream os = null; // 得到文件输出流
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // 定义一个缓冲区
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush(); // 确保数据写入到磁盘当中
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
