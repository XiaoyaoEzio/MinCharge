package com.min.charge.controller;

import com.min.charge.json.JsonResult;
import com.min.charge.service.MicroPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MicroPayController {

	@Autowired
	MicroPayService microPayService;

	@RequestMapping("/min/micropay/pay")
	public @ResponseBody JsonResult pay(HttpServletRequest request,
			@RequestParam(name = "tradingSn",required = false) String tradingSn,
			@RequestParam(name = "token",required = true) String token,
			@RequestParam(name = "rechargeFee", defaultValue = "0", required =true ) int rechargeFee,
			@RequestParam(name = "openId", required = true) String openId) {
		
		JsonResult 	jResult = microPayService.pay(request,token, tradingSn, rechargeFee, openId);
		return jResult;
	}
	
	
}
