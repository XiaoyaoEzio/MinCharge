package com.min.charge.http;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpMethod {

	public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");
	
	public static String post(String url, String param) throws Exception{
		String result = null;
		OkHttpClient client = new OkHttpClient();
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
		OkHttpClient client = new OkHttpClient();
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
