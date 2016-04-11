package com.zdxh.music.bean;


import java.io.Serializable;

public class AuditionListBean implements Serializable {
	private String duration;
	private String typeDescription;
	private String url;
	private String size;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "AuditionListBean{" +
				"duration='" + duration + '\'' +
				", typeDescription='" + typeDescription + '\'' +
				", url='" + url + '\'' +
				", size='" + size + '\'' +
				'}';
	}
}
