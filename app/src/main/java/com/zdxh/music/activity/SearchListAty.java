package com.zdxh.music.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.util.ArrayLengthCallBackListener;
import com.zdxh.music.util.DataBeanAdapter;
import com.zdxh.music.util.Search;

import java.util.ArrayList;

/**
 * Created by huangchuzhou on 2016/4/8.
 */
public class SearchListAty extends Activity {
    private ListView searchListView;
    private DataBeanAdapter adapter;

    private ArrayList<EntityBean> entityBeanArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_aty_layout);


        searchListView = (ListView) findViewById(R.id.search_listView);

        initDataBean();
        /**
         * 主线程休眠1s,使得另一个线程先执行，dataBeanList才能被重新赋值
         * 这里的另一个线程指的是对网络数据的读取而开启的线程
         */

        try {
            Thread.sleep(1000);
            adapter = new DataBeanAdapter(SearchListAty.this,R.layout.search_list_aty_item, entityBeanArrayList);
            searchListView.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    //初始化DataBean这个实体类
    private void initDataBean() {

        String searchName = getIntent().getStringExtra("searchName");

        Search mSearch = new Search(searchName);
        mSearch.getData(new ArrayLengthCallBackListener() {
            @Override
            public void info(ArrayList<EntityBean> entityBeans) {
                entityBeanArrayList = entityBeans;

            }
        });


    }
}
