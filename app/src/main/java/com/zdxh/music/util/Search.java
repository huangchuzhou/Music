package com.zdxh.music.util;

import android.content.Context;

import com.google.gson.Gson;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.bean.ImageBean;
import com.zdxh.music.db.MusicDB;

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
    //获取EntityBeanData
    //http://search.dongting.com/song/search/old?q=%E7%8E%8B%E8%8F%B2&page=1&size=20
    public void getEntityBeanData(final Context mContext, final EntityBeanBackListener infoListener) {
        final MusicDB musicDB = MusicDB.getInstance(mContext);

        final String songUrl = "http://search.dongting.com/song/search/old?q="+encodeSearchName(searchName)+"&page=1&size=20";
        //如果数据库不存在这条数据
        if (!musicDB.isExists(songUrl)){

            //使用Gson解析json数据
            final Gson gson = new Gson();

            //此处搜索获得的是 EntityBean songUrl
            HttpUtil.parseJson(songUrl, new HttpCallbackListener() {
                @Override
                public void onFinish(String responce) {

                    EntityBean mEntityBean = gson.fromJson(responce,EntityBean.class);


                    List<EntityBean.DataBean> mDataBeanList = mEntityBean.getData();



                    for (int i = 0; i<mDataBeanList.size();i++){

                        EntityBean.DataBean dataBean = mDataBeanList.get(i);

                        //先判断能否存储到数据库中
                        if (!musicDB.isExistSongId(dataBean.getSong_id())){

                            //将EntityBean.DataBean解析的数据存储到数据库
                            mEntityBean.setSong(songUrl);
                            musicDB.saveEntityBean(mEntityBean);
                            //从数据库中加载数据
                            infoListener.info(musicDB.loadEntityBean(songUrl));
                        }

                    }

                }
                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }else {  //若存在这条数据
            infoListener.info(musicDB.loadEntityBean(songUrl));
        }


    }

    //获取ImageBeanData
    public void getImageBeanData(final Context mContext, final ImageBeanCallBackListener infoListener){
        final String imageUrl = "http://lp.music.ttpod.com/pic/down?artist="+encodeSearchName(searchName);

            final Gson gson = new Gson();
            HttpUtil.parseJson(imageUrl, new HttpCallbackListener() {
                @Override
                public void onFinish(String responce) {
                    ImageBean mImageBean = gson.fromJson(responce, ImageBean.class);
                    infoListener.info(imageUrl, mImageBean);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
    }


}
