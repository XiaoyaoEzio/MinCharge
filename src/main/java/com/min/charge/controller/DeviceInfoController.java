package com.min.charge.controller;

import com.min.charge.json.JsonResult;
import com.min.charge.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DeviceInfoController {

	@Autowired
	private DeviceInfoService deviceInfoService;
	
	@RequestMapping("/min/charge/connect")
	@ResponseBody
	public JsonResult getInfo(HttpServletRequest request,
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true ) String deviceSn){
		JsonResult jResult = deviceInfoService.getDeviceInfo(token, deviceSn);
		return jResult;
	}
	
	
}
