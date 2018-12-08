package com.min.charge.http.api;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.config.Config;
import com.min.charge.security.XMLUtil;

public class ChargeApi {
	
	private static final Logger logger = Logger.getLogger(ChargeApi.class);
	
	public static String operator( String command, String deviceSn, String path){
		CommandBean commandBean = new CommandBean();
		commandBean.setAppid(Config.Instance.sys_AppId);
		commandBean.setAttach("");
		commandBean.setCommand(command);
		commandBean.setDevice_id(deviceSn);
		commandBean.setDevice_path(path);
		commandBean.setMchid(Config.Instance.sys_MchId);
		commandBean.setNonce_str("123456");
		commandBean.setOpenid("oj9EwwBsPY0wJCr7mvixdJFz7IBg");
		commandBean.setSign_type("MD5");
		commandBean.setTime(String.valueOf(new Date().getTime()/1000));
		SortedMap<String, String> map = new TreeMap<String, String>();
		map.put("appid", Config.Instance.sys_AppId);
		map.put("attach", "");
		map.put("command", command);
		map.put("device_id", deviceSn);
		map.put("device_path", path);
		map.put("mchid", Config.Instance.sys_MchId);
		map.put("nonce_str", "123456");
		map.put("openid", "oj9EwwBsPY0wJCr7mvixdJFz7IBg");
		map.put("sign_type", "MD5");
		map.put("time", commandBean.getTime());
		String sign = XMLUtil.createSign(map, Config.Instance.sys_ApiKey);
		commandBean.setSign(sign);
		System.err.println("sign: "+sign);
		ObjectMapper objectMapper = new ObjectMapper();
		String send ="";
		try {
			send = objectMapper.writeValueAsString(commandBean);
			System.err.println(send);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(),e);
		}
		return send;
	}
	
	public static void  main(String[] arg0) {
//		ChargeApi.operator("start", "");
	}
}
