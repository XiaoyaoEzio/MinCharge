package com.min.charge.service;

import com.min.charge.json.JsonResult;

import javax.servlet.http.HttpServletRequest;

public interface MicroPayService {
	
	JsonResult pay(HttpServletRequest request,String token, String tradingNo, int rechargeValue, String openId);
}
