package com.min.charge.http;

import okhttp3.*;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class HttpMethod {

	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	public static String post(String url, String param) throws Exception{
		String result = null;
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(30L, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.build();
		RequestBody postBody = RequestBody.create(JSON,param);
		Request request = new Request.Builder()
				.url(url).post(postBody).build();
	    Response response = client.newCall(request).execute();
	    ResponseBody body = response.body();
    
	    InputStream bufferStream = body.byteStream();
	    byte[] buffer = new byte[1024];
	    int hasRead =0;
	    while ((hasRead = bufferStream.read(buffer))>0) {
			byte[] rea = new String(buffer,0,hasRead).getBytes("utf-8");
	    	result = new String(rea);
	    	
		}
		return result;
	}
	
	public static String get(String url, String param) throws Exception{
		String result = null;
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(30L, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.build();
		Request request = new Request.Builder()
        .url(url).build();
	 
	    Response response = client.newCall(request).execute();
	    ResponseBody body = response.body();
    
	    InputStream bufferStream = body.byteStream();
	    byte[] buffer = new byte[1024];
	    int hasRead =0;
	    while ((hasRead = bufferStream.read(buffer))>0) {
			byte[] rea = new String(buffer,0,hasRead).getBytes("utf-8");
	    	result = new String(rea);
	    	
		}
		return result;
	}
}
