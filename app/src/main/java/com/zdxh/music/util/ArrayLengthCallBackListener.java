package com.zdxh.music.util;

import com.zdxh.music.bean.EntityBean;

import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/8.
 */

//此接口用于回调查询到的数据(歌手歌曲信息)
public interface ArrayLengthCallBackListener {
    void info(ArrayList<EntityBean> entityBeans);
}
