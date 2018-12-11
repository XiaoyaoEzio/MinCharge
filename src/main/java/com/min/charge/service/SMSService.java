package com.min.charge.service;

import com.min.charge.json.JsonResult;

import javax.servlet.http.HttpServletRequest;

public interface SMSService {

	/**
	 * 
	 * @param request
	 * @param mobile 手机号
	 * @param token token
	 * @param sign 签名
	 * @param timeStamp 时间
	 * @param type 类型：1：注册；2：修改验证码；3：忘记密码
	 * @return
	 */
	JsonResult sendVCode(HttpServletRequest request,  String mobile, String token, String sign, String timeStamp, int type);
}
