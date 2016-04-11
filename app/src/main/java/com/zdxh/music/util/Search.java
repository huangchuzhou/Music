package com.zdxh.music.util;

import android.util.Log;

import com.zdxh.music.bean.AuditionListBean;
import com.zdxh.music.bean.DataBean;
import com.zdxh.music.bean.MusicBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 此类主要用于搜索
 */
public class Search {
    private String searchName;  //搜索的歌名或者歌手名

    private ArrayList<DataBean> list = new ArrayList<>();
    public Search(String searchName){
        this.searchName = searchName;
    }

    //对输入的包含中文信息的语句进行编码
    private String encodeSearchName(String searchName){
        String encodeSearchName = null;
        try {
            encodeSearchName = URLEncoder.encode(searchName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeSearchName;
    }
    public void getData(final ArrayLengthCallBackListener infoListener) {

        String url = "http://search.dongting.com/song/search/old?q="+encodeSearchName(searchName);

        HttpUtil.parseJson(url, new HttpCallbackListener() {
            MusicBean musicBean = new MusicBean();

            @Override
            public void onFinish(String responce) {
                try {
                    JSONObject MusicJson = new JSONObject(responce);
                    musicBean.setCode(MusicJson.getInt("code"));
                    musicBean.setRow(MusicJson.getString("rows"));

                    JSONArray dataArray = MusicJson.getJSONArray("data");
                    Log.d("TAG",dataArray.length()+"");
                    for (int i = 0; i < dataArray.length(); i++) {
                        DataBean dataBean = new DataBean();
                        JSONObject dataJson = dataArray.getJSONObject(i);
                        dataBean.setSong_id(dataJson.getInt("song_id"));
                        dataBean.setSinger_id(dataJson.getInt("singer_id"));
                        dataBean.setSinger_name(dataJson.getString("singer_name"));
                        dataBean.setSong_name(dataJson.optString("song_name"));

                        JSONArray auditionListArray = dataJson.getJSONArray("audition_list");

                        list.add(dataBean);
                        infoListener.info(list);


                        for (int j = 0; j < auditionListArray.length(); j++) {
                            AuditionListBean auditionListBean = new AuditionListBean();
                            JSONObject auditionListJson = auditionListArray.getJSONObject(j);
                            auditionListBean.setDuration(auditionListJson.getString("duration"));
                            auditionListBean.setTypeDescription(auditionListJson.getString("typeDescription"));
                            auditionListBean.setUrl(auditionListJson.getString("url"));
                            auditionListBean.setSize(auditionListJson.getString("type"));
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(final Exception e) {
                e.printStackTrace();
            }
        });

    }
}
