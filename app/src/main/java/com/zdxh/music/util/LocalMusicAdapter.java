package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.mp3.Mp3Info;

import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/12.
 */
public class LocalMusicAdapter extends ArrayAdapter<Mp3Info> {
    private int resourceId;
    public LocalMusicAdapter(Context context, int resource, List<Mp3Info> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.songName = (TextView) convertView.findViewById(R.id.songName);
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.singName);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.tvList_Duration);
            viewHolder.num = (TextView) convertView.findViewById(R.id.tvNumber);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Mp3Info mp3Info = getItem(position);
        viewHolder.num.setText(position+1+"");
        viewHolder.songName.setText(mp3Info.getSong_Name());
        viewHolder.singerName.setText(mp3Info.getSinger_name());
        viewHolder.duration.setText(mp3Info.getDuration());
        return convertView;
    }

    class ViewHolder{
        TextView songName;
        TextView singerName;
        TextView duration;
        TextView num;
    }
}
