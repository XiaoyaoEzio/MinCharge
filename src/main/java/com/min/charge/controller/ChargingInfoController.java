package com.min.charge.controller;

import com.min.charge.json.JsonResult;
import com.min.charge.service.ChargingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChargingInfoController {
	
	@Autowired
	ChargingInfoService chargingInfoService;

	@ResponseBody
	@RequestMapping("/min/charge/refresh")
	public JsonResult refresh(HttpServletRequest request ,
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String deviceSn){
		JsonResult jResult = chargingInfoService.refresh(token, deviceSn);
		return jResult;
	}
	
	@ResponseBody
	@RequestMapping("/min/charge/charging")
	public JsonResult myCharge(HttpServletRequest request ,
			@RequestParam(name = "token", required = true) String token){
		JsonResult jResult = chargingInfoService.refresh(token,null);
		return jResult;
	}
}
