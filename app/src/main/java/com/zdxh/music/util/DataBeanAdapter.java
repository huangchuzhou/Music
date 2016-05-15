package com.zdxh.music.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.application.MusicApplication;
import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.bean.GeCiBean;
import com.zdxh.music.db.MusicDB;

import java.io.File;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 此类是DataBean实体类的适配器
 */
public class DataBeanAdapter extends ArrayAdapter<EntityBean>{
    private int resourceID;
    private Context mContext;
    private LayoutInflater inflater;
    public DataBeanAdapter(Context context, int resource, List<EntityBean> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceID = resource;
        inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder viewHolder;
        View view;

        final EntityBean mEntityBean = getItem(position);

        List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();

        if (convertView == null) {
            //动态加载布局
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new MyViewHolder();
            viewHolder.singName = (TextView) view.findViewById(R.id.singName);
            viewHolder.songName = (TextView) view.findViewById(R.id.songName);
            viewHolder.tvNum = (TextView) view.findViewById(R.id.tvNum);
            viewHolder.tvList_Duration = (TextView) view.findViewById(R.id.tvList_Duration);
            viewHolder.other = (LinearLayout) view.findViewById(R.id.layout_other);
            viewHolder.collection = (LinearLayout) view.findViewById(R.id.item_collection);
            viewHolder.add = (LinearLayout) view.findViewById(R.id.item_add);
            viewHolder.share = (LinearLayout) view.findViewById(R.id.item_share);
            viewHolder.download = (LinearLayout) view.findViewById(R.id.item_download);
            view.setTag(viewHolder);

        } else {

            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }

        final EntityBean.DataBean mDataBean = dataBeanList.get(position);
        viewHolder.tvNum.setText(position+1+"");
        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();
        final EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(position);
        viewHolder.songName.setText(mDataBean.getSong_name());
        viewHolder.singName.setText(mDataBean.getSinger_name());
        viewHolder.tvList_Duration.setText(mAuditionListBean.getDuration());

        if (position== SearchListAty.currentPosition&& MusicApplication.isShowOther == true){

            viewHolder.other.setVisibility(View.VISIBLE);
            viewHolder.add.setClickable(true);
            viewHolder.collection.setClickable(true);
            viewHolder.share.setClickable(true);
            viewHolder.download.setClickable(true);

            //收藏
            viewHolder.collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"已收藏",Toast.LENGTH_SHORT).show();
//                    Intent mIntent = new Intent();
//                    mIntent.setAction(MediaService.COLLECTION);
//                    getContext().sendBroadcast(mIntent);
                    //收藏歌曲
                    MusicApplication.collections.add(mDataBean);

//                    for (EntityBean.DataBean databean: MusicApplication.collections) {
//                        if (databean == mDataBean){
//                            MusicApplication.isCollection = true;
//                            Intent intent = new Intent();
//                            intent.setAction(MediaService.COLLECTION);
//                            getContext().sendBroadcast(intent);
//                        }else {
//                            MusicApplication.isCollection = false;
//                            Intent intent = new Intent();
//                            intent.setAction(MediaService.COLLECTION);
//                            getContext().sendBroadcast(intent);
//                        }
//                    }
                }
            });

            //下载
            viewHolder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //先判断在内存card中是否有相应的歌曲
                    File file = new File(DownloadMp3.DOWNLOAD_PATH,mDataBean.getSong_id()+".mp3");
                    if (file.exists()){
                        Toast.makeText(mContext,"文件已经存在",Toast.LENGTH_SHORT).show();
                    }else {

                        if (MusicApplication.isNetworkAvailable){  //网络可用
                            //开始下载歌曲
                            Toast.makeText(mContext,"开始下载",Toast.LENGTH_SHORT).show();
                            new DownloadMp3(mDataBean,mAuditionListBean).start(); //下载MP3文件
//                            new DownloadLrc()
                            //保存数据到数据库中
                            if (!MusicDB.musicDB.isExistSongId(mDataBean.getSong_id())){

                                MusicDB.musicDB.saveEntityBean(mEntityBean);
                            }
                            Toast.makeText(mContext,"下载完成",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext,"木有网络...",Toast.LENGTH_SHORT).show();
                        }

                    }

                    Search search = new Search();
                    search.getLrcBeanData(mDataBean, new GeCiBeanCallBackListener() {

                        @Override
                        public void info(GeCiBean geCiBean) {
                            if (geCiBean.getCode()==0){ //有相应的lrc文件
                                List<GeCiBean.ResultBean> resultBeens = geCiBean.getResult();
                                GeCiBean.ResultBean resultBean = resultBeens.get(0);

                                //判断数据库中是否有这条数据
                                if (!MusicDB.musicDB.isExistsLrc(resultBean.getAid())){
                                    new DownloadLrc(resultBean).start();  //下载lrc文件
                                    MusicDB.musicDB.saveResultBean(resultBean,mDataBean);
                                }

                            }

                        }
                    });
                    //下载歌曲
//                    new DownloadThread(songUrl,2,mDataBean.getSong_id()).start();
                }
            });

        }else {
            viewHolder.other.setVisibility(View.GONE);
            viewHolder.add.setClickable(false);
            viewHolder.collection.setClickable(false);
            viewHolder.share.setClickable(false);
            viewHolder.download.setClickable(false);
        }
        return view;
    }


    class MyViewHolder{
        public TextView tvNum;
        public TextView songName;
        public TextView singName;
        public TextView tvList_Duration;
        public LinearLayout other;
        public LinearLayout collection;
        public LinearLayout add;
        public LinearLayout download;
        public LinearLayout share;

    }

}
