package com.min.charge.controller;


import com.min.charge.json.JsonResult;
import com.min.charge.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SMSController {

	@Autowired
	SMSService sMSService;
	
	
	@RequestMapping("/min/sms/get")
	public @ResponseBody JsonResult action(HttpServletRequest request, 
			@RequestParam(name = "phoneNum", required = true) String mobile,
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "sign", defaultValue = "32", required = true) String sign,
			@RequestParam(name = "timeStamp", required = true) String timeStamp,
			@RequestParam(name = "type", defaultValue = "1", required = true) int type){
		
		JsonResult jResult = sMSService.sendVCode(request, mobile, token, sign, timeStamp, type);
		return jResult;
	}
	
}
