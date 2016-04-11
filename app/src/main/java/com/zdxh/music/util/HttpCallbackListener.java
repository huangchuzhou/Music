package com.zdxh.music.util;

//回调接口，实现此接口获得服务器返回的数据
public interface HttpCallbackListener {
	void onFinish(String responce);
	void onError(Exception e);
}
