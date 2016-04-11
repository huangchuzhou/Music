package com.zdxh.music.bean;

import java.io.Serializable;
import java.util.List;

public class MusicBean implements Serializable {
	private int code;
	private List<DataBean> data;
	private String row;
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	private String pages;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public List<DataBean> getData() {
		return data;
	}
	public void setData(List<DataBean> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "MusicBean{" +
				"code=" + code +
				", data=" + data +
				", row='" + row + '\'' +
				", pages='" + pages + '\'' +
				'}';
	}
}
