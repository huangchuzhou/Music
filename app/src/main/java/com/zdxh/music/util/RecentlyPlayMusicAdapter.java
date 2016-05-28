package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/13.
 */
public class RecentlyPlayMusicAdapter extends ArrayAdapter<EntityBean.DataBean> {
    private int resourceId;
    public RecentlyPlayMusicAdapter(Context context, int resource, List<EntityBean.DataBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        EntityBean.DataBean dataBean = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.singerName);
            viewHolder.songName = (TextView) convertView.findViewById(R.id.songName);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvTimems = (TextView) convertView.findViewById(R.id.tvTimems);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvNumber.setText(position+1+"");
        viewHolder.singerName.setText(dataBean.getSinger_name());
        viewHolder.songName.setText(dataBean.getSong_name());
        //获取当前时间
        System.setProperty("user.timezone","GMT+8");
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatms = new SimpleDateFormat("HH:mm:ss");
        String time = simpleDateFormat.format(nowTime);
        String timems = simpleDateFormatms.format(nowTime);
        viewHolder.tvTime.setText(time);
        viewHolder.tvTimems.setText(timems);
        return convertView;
    }

    class ViewHolder{
        TextView singerName;
        TextView songName;
        TextView tvNumber;
        TextView tvTime;
        TextView tvTimems;

    }
}
