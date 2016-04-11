package com.zdxh.music.bean;

import java.io.Serializable;
import java.util.List;

public class DataBean implements Serializable{
	private String song_name;
	private String singer_name;
	private int song_id;
	private int singer_id;
	private List<AuditionListBean> audition_list;
	public String getSong_name() {
		return song_name;
	}
	public void setSong_name(String song_name) {
		this.song_name = song_name;
	}
	public String getSinger_name() {
		return singer_name;
	}
	public void setSinger_name(String singer_name) {
		this.singer_name = singer_name;
	}
	public int getSong_id() {
		return song_id;
	}
	public void setSong_id(int song_id) {
		this.song_id = song_id;
	}
	public int getSinger_id() {
		return singer_id;
	}
	public void setSinger_id(int singer_id) {
		this.singer_id = singer_id;
	}
	public List<AuditionListBean> getAudition_list() {
		return audition_list;
	}
	public void setAudition_list(List<AuditionListBean> audition_list) {
		this.audition_list = audition_list;
	}

	@Override
	public String toString() {
		return "DataBean{" +
				"song_name='" + song_name + '\'' +
				", singer_name='" + singer_name + '\'' +
				", song_id=" + song_id +
				", singer_id=" + singer_id +
				", audition_list=" + audition_list +
				'}';
	}
}

