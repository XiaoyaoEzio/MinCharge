package com.min.charge.config;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.min.charge.beans.SysParam;
import com.min.charge.mapping.SysParamMapper;


public enum Config {
	
	Instance;
	
	private static final Logger logger  = Logger.getLogger(Config.class);

	public String micro_MchId;
	
	public String micro_AppId;
	
	public String micro_ApiKey;
	
	public String sys_MchId;
	
	public String sys_AppId;
	
	public String  sys_ApiKey;
	
	public String micro_Qurey_URL;
	
	public String sms_AppId;

	public String sms_AccountSid;
	
	public String sms_AuthToken;
	
	public String sms_RegisterId;
	
	public String  sms_ResetId;
	
	public String micro_unified_URL;
	
	public String micro_query_URL;
	
	public String device_Query_URL = "http://api.cdxiongmaoyun.com/CloudPandaAPI/Command";
	
	public String pay_Notify_URL = "http://heiniu.superyc.com/MinCharge";
	
	public void loadConfig(){

		SqlSession sqlSession = MybaitsConfig.getCurrent();
		SysParamMapper sysParamMapper = sqlSession.getMapper(SysParamMapper.class);
		List<SysParam> listParam = sysParamMapper.getAll();
		for (SysParam item : listParam) {
			/******		微信配置加载		*****/
			if ("micro_MchId".equals(item.getParamKey())) {
				micro_MchId = item.getParamValue();
			}
			
			if ("micro_AppId".equals(item.getParamKey())) {
				micro_AppId = item.getParamValue();
			}
			
			if ("micro_ApiKey".equals(item.getParamKey())) {
				micro_ApiKey = item.getParamValue();
			}
			
			if ("micro_Qurey_URL".equals(item.getParamKey())) {
				micro_Qurey_URL = item.getParamValue();
			}
			if ("micro_unified_URL".equals(item.getParamKey())) {
				micro_unified_URL = item.getParamValue();
			}
			

			/******		系统配置加载	*****/
			if ("sys_MchId".equals(item.getParamKey())) {
				sys_MchId = item.getParamValue();
			}
			
			if ("sys_AppId".equals(item.getParamKey())) {
				sys_AppId = item.getParamValue();
			}
			
			if ("sys_ApiKey".equals(item.getParamKey())) {
				sys_ApiKey = item.getParamValue();
			}

			/******		短信配置加载		*****/
			if ("sms_AppId".equals(item.getParamKey())) {
				sms_AppId = item.getParamValue();
			}
			
			if ("sms_AccountSid".equals(item.getParamKey())) {
				sms_AccountSid = item.getParamValue();
			}
			
			if ("sms_AuthToken".equals(item.getParamKey())) {
				sms_AuthToken = item.getParamValue();
			}
			
			if ("sms_RegisterId".equals(item.getParamKey())) {
				sms_RegisterId = item.getParamValue();
			}
			
			if ("sms_ResetId".equals(item.getParamKey())) {
				sms_ResetId = item.getParamValue();
			}
			
			if ("device_Query_URL".equals(item.getParamKey())) {
				device_Query_URL = item.getParamValue();
			}
			
			if ("pay_Notify_URL".equals(item.getParamKey())) {
				pay_Notify_URL = item.getParamValue();
			}
			logger.error(item.getParamKey()+": "+item.getParamValue());
		}
		MybaitsConfig.closeCurrent();
	}
	
	
	
}
