package com.zdxh.music.util;

import com.google.gson.Gson;
import com.zdxh.music.bean.EntityBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 此类主要用于搜索
 */
public class Search {
    private String searchName;  //搜索的歌名或者歌手名

    private ArrayList<EntityBean> entityBeanArrayList = new ArrayList<>();
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

        //使用Gson解析json数据
        final Gson gson = new Gson();
        HttpUtil.parseJson(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String responce) {

                EntityBean mEntityBean = gson.fromJson(responce,EntityBean.class);

                List<EntityBean.DataBean> mDataBean = mEntityBean.getData();
                //获取EntityBean.DataBean集合的长度用于调用info这个方法来动态增长SearchListAty的item;
                for (int i = 0; i<mDataBean.size(); i++){
                    entityBeanArrayList.add(mEntityBean);
                    infoListener.info(entityBeanArrayList);

                }

            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }
}
