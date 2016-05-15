package com.zdxh.music.mp3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huangchuzhou on 2016/5/12.
 */
public class Mp3Info implements Parcelable {
    private String song_id;
    private String song_Name;
    private String singer_name;
    private String duration;
    private String size;

    public Mp3Info() {
    }

    public Mp3Info(String song_id, String song_Name, String singer_name, String duration, String size) {
        this.song_id = song_id;
        this.song_Name = song_Name;
        this.singer_name = singer_name;
        this.duration = duration;
        this.size = size;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public String getSong_Name() {
        return song_Name;
    }

    public void setSong_Name(String song_Name) {
        this.song_Name = song_Name;
    }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "song_id=" + song_id +
                ", song_Name='" + song_Name + '\'' +
                ", singer_name='" + singer_name + '\'' +
                ", duration='" + duration + '\'' +
                ", size='" + size + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.song_id);
        dest.writeString(this.song_Name);
        dest.writeString(this.singer_name);
        dest.writeString(this.duration);
        dest.writeString(this.size);
    }

    protected Mp3Info(Parcel in) {
        this.song_id = in.readString();
        this.song_Name = in.readString();
        this.singer_name = in.readString();
        this.duration = in.readString();
        this.size = in.readString();
    }

    public static final Creator<Mp3Info> CREATOR = new Creator<Mp3Info>() {
        @Override
        public Mp3Info createFromParcel(Parcel source) {
            return new Mp3Info(source);
        }

        @Override
        public Mp3Info[] newArray(int size) {
            return new Mp3Info[size];
        }
    };
}
