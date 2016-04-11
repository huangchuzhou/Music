package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.bean.DataBean;

import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 此类是DataBean实体类的适配器
 */
public class DataBeanAdapter extends ArrayAdapter<DataBean> {
    private int resourceID;
    private int num = 0;

    public DataBeanAdapter(Context context, int resource, List<DataBean> objects) {
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
        num++;
        DataBean dataBean = getItem(position);
        View view;
        myViewHolder viewHolder;
        if (convertView == null){
            //动态加载布局
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new myViewHolder();
            viewHolder.singName = (TextView) view.findViewById(R.id.singName);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.tvNum = (TextView)view.findViewById(R.id.tvNum);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (myViewHolder) view.getTag();
        }


        viewHolder.tvNum.setText(num+"");
        viewHolder.songName.setText(dataBean.getSong_name());
        viewHolder.singName.setText(dataBean.getSinger_name());

        return view;
    }

    class myViewHolder{
        TextView tvNum;
        TextView songName;
        TextView singName;
    }
}
