package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;

import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/13.
 */
public class CollectionMusicAdapter extends ArrayAdapter<EntityBean.DataBean> {
    private int resourceId;
    public CollectionMusicAdapter(Context context, int resource, List<EntityBean.DataBean> objects) {
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
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.singerName);
            viewHolder.songName = (TextView) convertView.findViewById(R.id.songName);
            viewHolder.btnCollection = (ImageButton) convertView.findViewById(R.id.btnCollection);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvNumber.setText(position+1+"");
        viewHolder.singerName.setText(dataBean.getSinger_name());
        viewHolder.songName.setText(dataBean.getSong_name());

        return convertView;
    }

    class ViewHolder{
        TextView tvNumber;
        TextView singerName;
        TextView songName;
        ImageButton btnCollection;
    }
}
