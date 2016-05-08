package com.zdxh.music.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zdxh.music.bean.EntityBean;
import com.zdxh.music.bean.GeCiBean;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangchuzhou on 2016/4/16.
 */
public class MusicDB {
    private DBHelper mDBHelper;
    private SQLiteDatabase db;
    public static MusicDB musicDB;
    private Context mContext;
    public MusicDB(Context context) {
        mDBHelper = new DBHelper(context);
        db = mDBHelper.getWritableDatabase();
        mContext = context;
    }

   // 获取MusicDB的实例
    public synchronized static MusicDB getInstance(Context context) {
        if (musicDB == null) {
            musicDB = new MusicDB(context);
        }
        return musicDB;
    }

    /**
     * 将EntityBean实例存储到数据库
     */

    // private static final String SQL_CREATE = "create table music(id integer primary key autoincrement,"
    // +
   // "song_id integer,song_name text,singer_name text,duration text,typeDescription,songUrl text" +
     //       "size text,imageUrl text lrcUrl)";
    public void saveEntityBean(EntityBean mEntityBean){
        if (mEntityBean != null){
            List<EntityBean.DataBean> dataBeanList = mEntityBean.getData();
            for (int i = 0;i<dataBeanList.size();i++){
                EntityBean.DataBean mDataBean = dataBeanList.get(i);
                List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = mDataBean.getAudition_list();
                EntityBean.DataBean.AuditionListBean mAuditionListBean = auditionListBeanList.get(0);
                ContentValues values = new ContentValues();
                values.put("song",mEntityBean.getSong());
                values.put("song_id",mDataBean.getSong_id());
                values.put("song_name",mDataBean.getSong_name());
                values.put("singer_name",mDataBean.getSinger_name());
                values.put("duration",mAuditionListBean.getDuration());
                values.put("typeDescription",mAuditionListBean.getTypeDescription());
                values.put("songUrl",mAuditionListBean.getUrl());
                values.put("size",mAuditionListBean.getSize());
                db.insert("entity",null,values);
            }
        }
    }

    /**
     * 从数据库中读取EntityBean的数据
     */

    public ArrayList<EntityBean> loadEntityBean(String song){
        ArrayList<EntityBean> entityBeanList = new ArrayList<>();
        List<EntityBean.DataBean> dataBeanList = new ArrayList<>();
        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from entity where song = ?",new String[]{song});
        if (cursor.moveToFirst()){
            do {
                EntityBean mEntityBean = new EntityBean();
                EntityBean.DataBean mDataBean = new EntityBean.DataBean();
                EntityBean.DataBean.AuditionListBean mAuditionListBean = new EntityBean.DataBean.AuditionListBean();
                mDataBean.setSong_name(cursor.getString(cursor.getColumnIndex("song_name")));
                mDataBean.setSinger_name(cursor.getString(cursor.getColumnIndex("singer_name")));
                mAuditionListBean.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
                mAuditionListBean.setSize(cursor.getString(cursor.getColumnIndex("size")));
                mAuditionListBean.setTypeDescription(cursor.getString(cursor.getColumnIndex("typeDescription")));
                mAuditionListBean.setUrl(cursor.getString(cursor.getColumnIndex("songUrl")));
                auditionListBeanList.add(mAuditionListBean);
                mDataBean.setAudition_list(auditionListBeanList);

                dataBeanList.add(mDataBean);
                mEntityBean.setData(dataBeanList);
                entityBeanList.add(mEntityBean);


            }while (cursor.moveToNext());
        }
        cursor.close();
        return entityBeanList;
    }

    /**

     * 从数据库中读取EntityBean.DataBean.AuditionListBean的数据
     */
//    public List<EntityBean.DataBean.AuditionListBean> loadAuditionListBeans(String songUrl){
//        List<EntityBean.DataBean.AuditionListBean> auditionListBeanList = new ArrayList<>();
//        EntityBean.DataBean.AuditionListBean mAuditionListBean = new EntityBean.DataBean.AuditionListBean();
//        Cursor cursor = db.rawQuery("select * from entity where songUrl = ?",new String[]{songUrl});
//        if (cursor.moveToFirst()){
//            do {
//                mAuditionListBean.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
//                mAuditionListBean.setSize(cursor.getString(cursor.getColumnIndex("size")));
//                mAuditionListBean.setTypeDescription(cursor.getString(cursor.getColumnIndex("typeDescription")));
//                mAuditionListBean.setUrl(cursor.getString(cursor.getColumnIndex("songUrl")));
//                auditionListBeanList.add(mAuditionListBean);
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//        return auditionListBeanList;
//    }
    /**
     * 将GeCiBean.ResultBean实例存储到数据库
     * lrc
     */
    public void saveResultBean(GeCiBean.ResultBean mResultBean){
        if (mResultBean != null){
            ContentValues values = new ContentValues();
            values.put("lrcUrl",mResultBean.getLrc());
            db.insert("entity",null,values);
        }
    }

    /**
     * 从数据库中读取GeCiBean.ResultBean的数据
     * lrc
     */
    public List<GeCiBean.ResultBean> loadResultBeans(){
        List<GeCiBean.ResultBean> resultBeanList = new ArrayList<>();
        GeCiBean.ResultBean mResultBean = new GeCiBean.ResultBean();
        Cursor cursor = db.query("entity",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                mResultBean.setLrc(cursor.getString(cursor.getColumnIndex("lrcUrl")));
                resultBeanList.add(mResultBean);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return resultBeanList;
    }

    /**
     * 将ImageBean.DataBean实例存储到数据库
     * image
     */
    public void saveImageDataBean(String imageUrl,String singerName,Bitmap singerPic){
        if (singerPic != null){

            ContentValues values = new ContentValues();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            singerPic.compress(Bitmap.CompressFormat.PNG,100,baos);//压缩为PNG格式,压缩率为0%
            byte[] bytes = baos.toByteArray();
            values.put("singerPic",bytes);
            values.put("imageUrl",imageUrl);
            values.put("singerName",singerName);
            db.insert("image",null,values);
        }
    }

    /**
     * 从数据库中读取ImageBean.DataBean的数据
     * image
     */
    public Bitmap loadImage(String imageUrl){
        Bitmap imageBitmap = null;
        Cursor cursor = db.rawQuery("select * from image where imageUrl = ?",new String[]{imageUrl});
        byte[] imageQuery=null;
        if (cursor.moveToFirst()){
            do {
                //将Blob数据转化为字节数组
                imageQuery = cursor.getBlob(cursor.getColumnIndex("singerPic"));

            }while (cursor.moveToNext());
            //将字节数组转化为位图
            imageBitmap= BitmapFactory.decodeByteArray(imageQuery, 0, imageQuery.length);
        }

        cursor.close();
        return imageBitmap;
    }

    /**
     * 插入数据库时查看数据库中entity表中是否有song对应的这条数据(搜索歌曲或歌手的地址)
     */

    public boolean isExists(String song) {
        Cursor cursor = db.rawQuery("select * from entity where song = ?",new String[]{song});
        boolean exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }

    /**
     * 插入数据库时查看数据库中entity表中是否有song_id对应的这条数据
     */
    public boolean isExistSongId(int songId) {
        Cursor cursor = db.rawQuery("select * from entity where song_id = ?",new String[]{songId+""});
        boolean exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }

    /**
     * 插入数据库时查看数据库中是否有imageUrl对应的图片
     */
    public boolean isExistsImage(String imageUrl){
        Cursor cursor = db.rawQuery("select * from image where imageUrl= ?",new String[]{imageUrl});
        boolean exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }


}
