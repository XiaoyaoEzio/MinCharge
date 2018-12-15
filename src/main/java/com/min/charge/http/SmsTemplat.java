package com.min.charge.http;

import com.min.charge.config.Config;
import com.min.charge.security.CommonTool;
import com.min.charge.security.MD5;
import org.apache.commons.net.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;







public class SmsTemplat {

	private static final Logger logger=Logger.getLogger(SmsTemplat.class);
	public static String version ="2014-06-30";
	public static String server = "api.ucpaas.com";
//	public static String accountSid="9716bae35fbe11322745beb477c42aa9";//3956df98801e0801f49f0bf5473fa56e ye//9716bae35fbe11322745beb477c42aa9
//	public static String authToken="bddd47a6c199136f449862a2e7901a4a";//2a0fb75ef8c008f7910d6f8fa3881f8c  ye//bddd47a6c199136f449862a2e7901a4a
//	public static String appId="cee2f32798654f6da00432d9969a6485";//b9e6dd49c580401bb0f3b46f98254599  ye// cee2f32798654f6da00432d9969a6485
//	public static String register_id="18964";  //注册
//	public static String change_id="18960";		//修改密码	
	public static String templateId="18964";
//	public static String to="15920294465";
//	private static String para=getVerificationCode();
	

	
	
	/**
	 * 获取验证码
	 * @return
	 */
	public static String getVerificationCode() {
		Random random = new Random();

		String result="";

		for(int i=0;i<6;i++){

		result+=random.nextInt(10);

		}
		String overTime="2";//超时时间，单位:分钟
		String param=result+","+overTime;
		
		return param;
	}
	
	/**
	 * 获取签名
	 * @param accountSid
	 * @param authToken
	 * @param timestamp
	 * @return
	 * @throws Exception
	 */
	public static String getSignature(String accountSid, String authToken,String timestamp) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = MD5.encrypt(sig,"UTF-8");
		return signature;
	}
	
	
	public static HttpResponse get(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient) throws Exception{
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", cType);//
		httpget.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
	
		String auth = CommonTool.bytes2String(Base64.encodeBase64(src.getBytes("UTF-8")));
		httpget.setHeader("Authorization",auth);
		HttpResponse response = httpclient.execute(httpget);
		return response;
	}
	
	public static HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = CommonTool.bytes2String(Base64.encodeBase64(src.getBytes("UTF-8")));
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        //查看返回值
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	
	//转换时间格式
	public static String dateToStr(Date date,String pattern) {
	       if (date == null || date.equals(""))
	    	 return null;
	       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	       return formatter.format(date);
	} 
	
	public static String templateSMS(String accountSid, String authToken,
			String appId, String templateId, String to, String param) {
		String result = "";
		DefaultHttpClient httpclient=new DefaultHttpClient();
		try {
			
			//获取时间戳
			String timestamp = dateToStr(new Date(), "yyyyMMddHHmmss");//获取时间戳
			String signature =getSignature(accountSid,authToken,timestamp);
			StringBuffer sb = new StringBuffer("https://");
			sb.append(server);
			String url = sb.append("/").append(version)
					.append("/Accounts/").append(Config.Instance.sms_AccountSid)
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setParam(param);		
			String body="<templateSMS>";
			body=body+"<appId>"+templateSMS.getAppId()+"</appId>";
			body=body+"<templateId>"+templateSMS.getTemplateId()+"</templateId>";
			body=body+"<to>"+templateSMS.getTo()+"</to>";
			body=body+"<param>"+templateSMS.getParam()+"</param>";
			body=body+"</templateSMS>";
//			body="{\"templateSMS\":"+body+"}";
			logger.debug("Url："+url+"\n"+"post bpdy is: " + body);

			HttpResponse response=post("application/xml",accountSid, authToken, timestamp, url, httpclient, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
				System.out.println("templateSMS Response content is: " + result);
			}
			// 确保HTTP响应内容全部被读出或者内容流被关闭
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			//关闭连接
		    httpclient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	/**
	 * 短信验证码简易接口*/
//	public static String templateSimpleSMS(String accountSid, String authToken,
//			String appId, String templateId, String to, String param) {
//		String result = "";
//		String url = "http://www.ucpaas.com/maap/sms/code";
//		String timestamp = dateToStr(new Date(), "yyyyMMddHHmmssSSS");//获取时间戳
//		DefaultHttpClient httpclient=new DefaultHttpClient();
//		try {
//			//MD5加密
//			EncryptUtil encryptUtil = new EncryptUtil();
//			String signature =getSignature(accountSid,timestamp,authToken,encryptUtil);
//			StringBuffer sb = new StringBuffer(url);
//			url = sb.append("?")
//					.append("&sid=").append(accountSid)
//					.append("&appId=").append(appId)
//					.append("&time=").append(timestamp)
//					.append("&sign=").append(signature.toLowerCase())
//					.append("&to=").append(to)
//					.append("&templateId=").append(templateId)
//					.append("&param=").append(param).toString();
//			System.out.println("templateSimpleSMS url = "+url);
//			HttpGet httpget = new HttpGet(url);
//
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//			if (entity != null) {
//				result = EntityUtils.toString(entity, "UTF-8");
//				System.out.println("templateSimpleSMS Response content is: " + result);
//			}
//			EntityUtils.consume(entity);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//			// 关闭连接
//		    httpclient.getConnectionManager().shutdown();
//		}
//		return result;
//	}
	
	
	public static void main(String[] args) throws IOException {
//		Config.getInstance().loadConfig();
//		server=Config.getInstance().getUcpaas_URL();
//		version=Config.getInstance().getUcpaas_version();
//		System.out.println(server);
////		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
////		String param=br.readLine();
////		String [] params=param.split(" ");
//		String method = "1";
//		String to="15920294465";
//		String para=getVerificationCode();
////		String para="15920294465,741258";
//		if (method.equals("1")) { //短信验证码 
//			templateSMS(BaseConfig.getUcpaas_accountSid(), BaseConfig.getUcpaas_authToken(),BaseConfig.getUcpaas_appId(), templateId, to, para);
//		}//else if(method.equals("2")){//短信验证码简易接口
////			templateSimpleSMS(accountSid, authToken,appId, templateId, to, para);
////		}
	}
}
