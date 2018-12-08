package com.min.charge.sevice.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Client;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.config.Config;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.enums.TradeTypeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.security.CommonTool;
import com.min.charge.security.MD5;
import com.min.charge.security.MicropayCore;
import com.min.charge.security.XMLUtil;
import com.min.charge.sevice.MicroPayService;

@Service
public class MicroPayServiceImpl implements MicroPayService{
	
	private static final Logger logger = Logger.getLogger(MicroNotifyServiceImpl.class);
	

	@Override
	public JsonResult pay(HttpServletRequest request,String token, String tradingNo, int rechargeValue, String openId) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		// 设置日期格式
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time=new Date();
		logger.debug(formatter.format(time));
		// 生成订单号
		BillRecords tradingLog = null;
		try {
			SqlSession session = MybaitsConfig.getCurrent();
			BillRecordsMapper billRecordsMaper = session.getMapper(BillRecordsMapper.class);
			if (null != tradingNo && tradingNo != "") {
				tradingLog =billRecordsMaper.getBySn(tradingNo);
				if (tradingLog==null) {
					return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
				}
				
				if (tradingLog.getTradeStatusEnum().compareTo(TradeStatusEnum.Waiting)!=0) {
					return JsonResult.code(ErrorCodeEnum.TRADINGSN_HAD_DONE);
				}
			} else {
				tradingLog = new BillRecords();
				tradingLog.setClientId(client.getId());
				tradingLog.setCreatedDateTime(time);
				tradingLog.setTradeStatusEnum(TradeStatusEnum.Waiting);
				tradingLog.setTradeType(TradeTypeEnum.RECHARGE);
				tradingLog.setTradingSn(CommonTool.GetTradingSn(time));
				tradingLog.setTotalFee(rechargeValue);
				billRecordsMaper.save(tradingLog);
				MybaitsConfig.commitCurrent();
			}
		} catch (Exception e) {
			
		}


		//支付完成后的回调处理页面
		String notify_url= Config.Instance.pay_Notify_URL;
//		String notify_url= "http://snmp012.idbksolar.com/MinCharge";
		String payNotify_url =notify_url+"/min/micropay/notify";
		
		JsonWeChat jWeChat=new JsonWeChat();
	
			//设置package订单参数
			String noncestr_str = "123456";
			
			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			packageParams.put("appid", Config.Instance.micro_AppId); //APPId
			packageParams.put("device_info", "APP-001"); //商品描述   
			packageParams.put("mch_id", Config.Instance.micro_MchId); //商户号   
			packageParams.put("body", "充值"); //商品描述   	
			packageParams.put("notify_url", payNotify_url); //接收微信通知的URL  
			packageParams.put("nonce_str", noncestr_str);
			packageParams.put("out_trade_no", tradingLog.getTradingSn()); //商家订单号  
			packageParams.put("total_fee", String.valueOf(rechargeValue)); //商品金额,以分为单位  
			packageParams.put("spbill_create_ip", "218.16.97.171"); //订单生成的机器IP，指用户浏览器端IP  
			packageParams.put("trade_type", "JSAPI");//交易类型
			packageParams.put("openid", openId);
			
			//生成获取prepayid的签名
			String sign="";
			sign = XMLUtil.createSign(packageParams, Config.Instance.micro_ApiKey);
			
			packageParams.put("sign",sign);
		
			
			//获取prepayId
			String prepayid = MicropayCore.sendPrepay(packageParams, Config.Instance.micro_ApiKey);

			if (null != prepayid && !"".equals(prepayid)) {

//			//设置支付参数
				String timestamp = MicropayCore.getTimeStamp();
				String noncestr=MD5.encrypt(timestamp, "UTF-8");
				SortedMap<String, String> signParams = new TreeMap<String, String>();
				signParams.put("appId", Config.Instance.micro_AppId);
				signParams.put("signType", "MD5");
				signParams.put("nonceStr", noncestr);
				signParams.put("package", "prepay_id="+prepayid);
				signParams.put("timeStamp", timestamp);
				//生成支付签名,进行MD5签名！
				String sign1="";
				sign1 = XMLUtil.createSign(signParams, Config.Instance.micro_ApiKey);
			

				jWeChat.appid = Config.Instance.micro_AppId;
				jWeChat.partnerid = Config.Instance.micro_MchId;//微信支付中partnerid即为商户号
				jWeChat.noncestr=noncestr;
				jWeChat.packages=prepayid;
				jWeChat.prepayid=prepayid;
				jWeChat.timestamp=timestamp;
				jWeChat.sign=sign1;
				jWeChat.tradingNo= tradingLog.getTradingSn();
				
			} else {
				System.err.println("错误：获取prepayId失败");
			}
			JsonResult jResult = new JsonResult();
			jResult.data = jWeChat;
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				System.err.println(objectMapper.writeValueAsString(jWeChat));
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
			}
		return jResult;
	}
	
	
	class JsonWeChat {
		
		public String appid;
		
		public String partnerid;
		
		public String noncestr;
		
		public String packages;
		
		public String prepayid;
		
		public String timestamp;
		
		public String sign;
		
		public String tradingNo;
	}
}
