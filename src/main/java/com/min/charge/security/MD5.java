package com.min.charge.security;

import java.security.MessageDigest;


public class MD5 {
	
	public final static String encrypt(String str) {
		byte[] result = null;
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			result = md5.digest(str.getBytes());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CommonTool.byte2Hex(result);
	}
	public final static String encrypt(byte[] bytes) {
		byte[] result = null;
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			result = md5.digest(bytes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CommonTool.byte2Hex(result);
	}
	
	public final static byte[] encryptAsBytes(byte[] bytes){
		byte[] result = null;
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			result = md5.digest(bytes);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public final static String encrypt(String str, String charset) {
		byte[] result = null;
		try{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			result = md5.digest(str.getBytes(charset));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return CommonTool.byte2Hex(result);
	}
}
