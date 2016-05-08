package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdxh.music.R;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.db.MusicDB;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/5/5.
 */
public class EnjoyAdapter extends ArrayAdapter<EntityBean.DataBean> {
    private int resourceId;


    public EnjoyAdapter(Context context, int resource, List<EntityBean.DataBean> objects) {
        super(context, resource, objects);
        resourceId = resource;
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
        EntityBean.DataBean mDataBean = getItem(position);

        if (convertView == null) {
            //动态加载布局
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new MyViewHolder();
            viewHolder.singName = (TextView) view.findViewById(R.id.singName);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.tvNum = (TextView) view.findViewById(R.id.tvNum);
            viewHolder.ivAlbum = (ImageView) view.findViewById(R.id.ivAlbum);
            view.setTag(viewHolder);

        } else {

            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }
        // TODO: 2016/5/6 这里的列表长度取决于databean的长度，有可能出现数组越界异常 
        viewHolder.tvNum.setText(position+1+"");
        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();
        EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(position);
        viewHolder.songName.setText(mDataBean.getSong_name());
        viewHolder.singName.setText(mDataBean.getSinger_name());
        String singerName = mDataBean.getSinger_name();
        String imageUrl = "http://lp.music.ttpod.com/pic/down?artist="+encodeSearchName(singerName);
        if (MusicDB.musicDB.isExistsImage(imageUrl)){
            viewHolder.ivAlbum.setImageBitmap(MusicDB.musicDB.loadImage(imageUrl));
        }else {
            viewHolder.ivAlbum.setImageResource(R.drawable.music_icon);
        }
        return view;
    }

    class MyViewHolder{
        TextView tvNum;
        TextView songName;
        TextView singName;
        ImageView ivAlbum;
    }
    private String encodeSearchName(String searchName){
        String encodeSearchName = null;
        try {
            encodeSearchName = URLEncoder.encode(searchName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeSearchName;
    }
}
