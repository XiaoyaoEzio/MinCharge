package com.min.charge.sevice;

import javax.servlet.http.HttpServletRequest;

import com.min.charge.json.JsonResult;

public interface MicroPayService {
	
	JsonResult pay(HttpServletRequest request,String token, String tradingNo, int rechargeValue, String openId);
}
