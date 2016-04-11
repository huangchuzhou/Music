package com.zdxh.music.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class HttpUtil {
	public static void parseJson(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {

				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					InputStream is = connection.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line;
					StringBuilder builder = new StringBuilder();
					while ((line = br.readLine())!= null) {
						builder.append(line);
						
					}
					
					br.close();
					isr.close();
					is.close();
					if (listener != null) {
						listener.onFinish(builder.toString());
					}
					builder.toString();
					
				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				}finally{
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}

}
