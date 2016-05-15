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
 * Created by huangchuzhou on 2016/5/13.
 */
public class DownloadMusicAdapter extends ArrayAdapter<Mp3Info> {
    private int resourceId;
    public DownloadMusicAdapter(Context context, int resource, List<Mp3Info> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Mp3Info mp3Info = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.singerName);
            viewHolder.songName = (TextView) convertView.findViewById(R.id.songName);
            viewHolder.tvList_Duration = (TextView) convertView.findViewById(R.id.tvList_Duration);
            viewHolder.tvMusicSize = (TextView) convertView.findViewById(R.id.tvMusicSize);
            viewHolder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvNumber.setText(position+1+"");
        viewHolder.tvMusicSize.setText(mp3Info.getSize());
        viewHolder.tvList_Duration.setText(mp3Info.getDuration());
        viewHolder.singerName.setText(mp3Info.getSinger_name());
        viewHolder.songName.setText(mp3Info.getSong_Name());
        return convertView;
    }

    class ViewHolder{
        TextView tvNumber;
        TextView songName;
        TextView singerName;
        TextView tvList_Duration;
        TextView tvMusicSize;

    }
}
