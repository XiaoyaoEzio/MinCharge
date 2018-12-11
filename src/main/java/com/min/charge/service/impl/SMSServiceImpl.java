package com.min.charge.service.impl;

import com.min.charge.beans.Client;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.config.Config;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.http.SmsTemplat;
import com.min.charge.json.JsonResult;
import com.min.charge.security.CommonTool;
import com.min.charge.security.MD5;
import com.min.charge.security.XMLUtil;
import com.min.charge.service.SMSService;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

@Service
public class SMSServiceImpl implements SMSService{
	private static final Logger logger = Logger.getLogger(SMSServiceImpl.class); 
	private String mobile = "15920294465";
	private String para = SmsTemplat.getVerificationCode();
	public static final Map<String, String> map = new HashMap<String, String>();

	@Override
	public JsonResult sendVCode(HttpServletRequest request, String mobile, String token, String sign, String timeStamp, int type) {
		if (sign.length() > 5) {
			try {
				String para = "phoneNum=" + mobile + "&timeStamp=" + timeStamp;
				String buffer = MD5.encrypt(para.getBytes("UTF-8"));
				String mac = CommonTool.encrypt(buffer, "EANAzDr0eLDVwiv5dBM535xpsPQ8Ny6-");
				if (!mac.equals(sign)) {
					logger.error("\n" + "本地签名:" + mac + "\n" 
							+ "客户端签名：" + sign + "\n"
							+ "签名字符串： " + para + "\n"
							+ "MD5结果： " + buffer);
					
					return JsonResult.code(ErrorCodeEnum.SMS_SIGNED_ERROR);
				}
			} catch (Exception e1) {
				logger.error(e1.getMessage(), e1);
				return JsonResult.code(ErrorCodeEnum.SMS_SIGNED_ERROR);
			}
		}
		
		String templateId = Config.Instance.sms_RegisterId;
		if (type == 2) {
			Client client = LoginBuffer.getClient(token);
			if (client == null) 
				return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);			
			templateId = Config.Instance.sms_RegisterId;
		}else if (type == 3) {
			templateId = Config.Instance.sms_RegisterId;
		}
		
		this.mobile = mobile;
		startRemove();
		String respon = SmsTemplat.templateSMS(Config.Instance.sms_AccountSid,
				Config.Instance.sms_AuthToken, Config.Instance.sms_AppId, templateId,
				mobile, SmsTemplat.getVerificationCode());
		try {
			SortedMap<String, String> param = XMLUtil.doXMLParse(respon);
			if (!(param.get("respCode").equals("000000"))) {
			 if (param.get("respCode").equals("100015")||
					param.get("respCode").equals("100006")) {
				return JsonResult.code(ErrorCodeEnum.PHONENUM_ILLEGAL);
			}else if (param.get("respCode").equals("100008")) {
				return JsonResult.code(ErrorCodeEnum.PHONENUM_LENTH_ERROR);
			}else {
				return JsonResult.code(ErrorCodeEnum.SMS_SEND_FAILED);
			}
		 }
		} catch (JDOMException e) {

			logger.error("XML解析出错" + e.getMessage(), e);
			return JsonResult.code(ErrorCodeEnum.SMS_SEND_FAILED);
		} catch (IOException e) {

			logger.error("解析XML初始化错误" + e.getMessage(), e);
			return  JsonResult.code(ErrorCodeEnum.SMS_SEND_FAILED);
		}
		return new JsonResult();
	}

	/**
	 * 缓存验证码，超过有效期并移除
	 */
	public  void startRemove() {

		Thread clearingThread = new Thread(new Runnable() {

			public void run() {
//				synchronized(map){
				String[] params = para.split(",");
				if (map.get(mobile) == null) {

					map.put(mobile, params[0]);

				} else {
					map.remove(mobile);
					map.put(mobile, params[0]);
				}

				Date begin = new Date();

//				String item = map.get(mobile);
				try {
					Thread.sleep(120 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String item = map.get(mobile);
				if (item!=null) {
					map.remove(mobile);
					Date finish = new Date();
					System.err.println("移除啦！！！" + mobile 
							+ "\n" + "内容：" + item
							+ "\n" + "begin :" + begin.toString() + "\n"
							+ "finish :" + finish.toString());
				}
				System.err.println("早已移除啦！！！");
				
				}
//			}
		});
		clearingThread.setName("startRemoveSMS");
		clearingThread.start();
	}	
}
