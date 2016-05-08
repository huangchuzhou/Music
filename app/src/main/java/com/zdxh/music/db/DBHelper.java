package com.zdxh.music.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huangchuzhou on 2016/4/16.
 * 数据库的创建帮助类
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DBNAME = "music.db";
    private static int VERSION = 1;
    private Context mContext;

    //建databean表语句
    private static final String SQL_CREATEENTITY = "create table entity(id integer primary key autoincrement," +
            "song_id integer,song_name text,singer_name text,duration text,typeDescription text,songUrl text," +
            "size text,song text)";
    private static final String SQL_CREATEIMAGE = "create table image(id integer primary key autoincrement," +
            "singerName text,imageUrl text,singerPic binary)"; //保存为二进制数据文件
    //删除表语句
    private static final String SQL_DROP_ENTITY  = "drop table if exists entity";
    private static final String SQL_DROP_IMAGE   = "drop table if exists entity";
    public DBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATEENTITY);
        db.execSQL(SQL_CREATEIMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(SQL_DROP_ENTITY);
        db.execSQL(SQL_DROP_IMAGE);
        db.execSQL(SQL_CREATEENTITY);
        db.execSQL(SQL_CREATEIMAGE);
//        deleteDatabase(mContext);

    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase("music.db");
    }


}
