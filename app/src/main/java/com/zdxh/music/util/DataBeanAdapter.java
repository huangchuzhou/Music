package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;

import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 此类是DataBean实体类的适配器
 */
public class DataBeanAdapter extends ArrayAdapter<EntityBean> {
    private int resourceID;

    public DataBeanAdapter(Context context, int resource, List<EntityBean> objects) {
        super(context, resource, objects);
        resourceID = resource;

    }

    /**
     * 重写父类的getView（）方法，这个方法在每一个子项被滚动到
     * 屏幕内的时候调用
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        View view;
        EntityBean mEntityBean = getItem(position);

        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();

        if (convertView == null) {
            //动态加载布局
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new MyViewHolder();
            viewHolder.singName = (TextView) view.findViewById(R.id.singName);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.tvNum = (TextView) view.findViewById(R.id.tvNum);
            viewHolder.tvList_Duration = (TextView) view.findViewById(R.id.tvList_Duration);
            view.setTag(viewHolder);

        } else {

            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }



            EntityBean.DataBean mDataBean = dataBeanList.get(position);
            viewHolder.tvNum.setText(position+1+"");
            List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();
            EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(position);
            viewHolder.songName.setText(mDataBean.getSong_name());
            viewHolder.singName.setText(mDataBean.getSinger_name());
            viewHolder.tvList_Duration.setText(mAuditionListBean.getDuration());


        return view;
    }

    class MyViewHolder{
        TextView tvNum;
        TextView songName;
        TextView singName;
        TextView tvList_Duration;

    }
}
