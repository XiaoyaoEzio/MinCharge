package com.min.charge.security;

import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.min.charge.config.Config;

public class MicropayCore {
	private static final Logger logger=Logger.getLogger(MicropayCore.class);
	
//	/**
//	 * 微信统一下单请求地址
//	 */
//	protected static String unified_order_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
//	
//	/**
//	 * 微信查询订单请求地址
//	 */
//	protected static String query_order_URL="https://api.mch.weixin.qq.com/pay/orderquery";
//	
	protected static String charset="UTF-8";
	/**
	 * 获取prepayid
	 * @param packageParams
	 * @param apiKey
	 * @return
	 */
	public static String sendPrepay(SortedMap<String,String> packageParams, String apiKey) {
		String prepayid = "";
		
		//生成要发送的XML数据
		String postData = XMLUtil.enXMLString(packageParams);
		
		//发送数据并获取微信返回的结果
		SortedMap<String,String> map=XMLUtil.postAndRecive(postData
											,Config.Instance.micro_unified_URL);
			
			//验签是否sign==send_sign
			String sign= XMLUtil.createSign(map, apiKey);
			
			String recive_sign=map.get("sign");
			if (map.get("return_code").equals("SUCCESS")&&map.get("return_msg").equals("OK")) {
				
				//验签是否sign==recive_sign
				if (sign.equals(recive_sign)) {
					
					prepayid=map.get("prepay_id");	
				}else {
					
				logger.error("验签失败："+"\n"+"sign:"+sign+"\n"+"recive_sign:"+recive_sign);
				}
			}else {
				logger.error("返回信息错误："+"\n"+"return_code："+map.get("return_code")
						+"\n"+"return_msg："+map.get("return_msg"));
			}
		return prepayid;
	}
	
	/**
	 * 查询订单的请求参数
	 * @param transaction_id
	 * @return
	 */
	public static String requestCheck(String transaction_id,String out_trade_no, String apiKey){
		SortedMap<String ,String> params=new TreeMap<String, String>();
		String nonce_str=getNonceStr();
		params.put("appid",Config.Instance.micro_AppId);
		params.put("mch_id",Config.Instance.micro_MchId);
		if (transaction_id.equals("")||transaction_id==null) {
			params.put("out_trade_no", out_trade_no);
			
		}else {
			params.put("transaction_id", transaction_id);
				
		}
		params.put("nonce_str",nonce_str);
		
		String sign =XMLUtil.createSign(params, apiKey);
		
		params.put("sign", sign);
		
		String postData=XMLUtil.enXMLString(params);
		logger.error("查询订单postData："+postData);
		return postData;
		
	}
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getNonceStr() {
		Random random = new Random();
		return MD5.encrypt(String.valueOf(random.nextInt(10000)), "UTF-8").toUpperCase();
	}
	/**
	 * 获取时间，以秒为单位
	 * @return
	 */
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 返回成功
	 * 
	 * @return
	 */
	public static String success() {

		StringBuffer sendStr = new StringBuffer();
		sendStr.append("<xml>");
		sendStr.append("<return_code>SUCCESS</return_code>");
		sendStr.append("<return_msg>OK</return_msg>");
		sendStr.append("</xml>");

		return sendStr.toString();

	}

	/**
	 * 返回失败
	 * 
	 * @return
	 */
	public static String fail() {
		// 定义一个返回的字符串BUFFER
		StringBuffer sendStr = new StringBuffer();
		sendStr.append("<xml>");
		sendStr.append("<return_code>FAIL</return_code>");
		sendStr.append("<return_msg>FAIL</return_msg>");
		sendStr.append("</xml>");

		return sendStr.toString();

	}
}
