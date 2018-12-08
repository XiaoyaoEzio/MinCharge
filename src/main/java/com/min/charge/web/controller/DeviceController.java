package com.min.charge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min.charge.json.JsonResult;
import com.min.charge.web.service.DeviceService;

@Controller
public class DeviceController {

	@Autowired
	DeviceService deviceService;
	
	@RequestMapping("min/web/device/save")
	public @ResponseBody JsonResult save(HttpServletRequest request,
			@RequestParam(name ="deviceSn", required = true) String deviceSn,
			@RequestParam(name ="deviceName", required = true) String deviceName,
			@RequestParam(name = "stationId",required = true) int stationId,
			@RequestParam(name ="webToken", required = true) String webToken) {
		
		JsonResult jsonResult =  deviceService.save(webToken, deviceSn, deviceName, stationId);
		return jsonResult;
	}
	
	@RequestMapping("min/web/device/query")
	public @ResponseBody JsonResult query(HttpServletRequest request,
			@RequestParam(name ="stationId", required = true) int stationId,
			@RequestParam(name ="pageIndex", defaultValue = "1", required = false) int pageIndex,
			@RequestParam(name ="pageSize", defaultValue = "10", required = false) int pageSize,
			@RequestParam(name ="webToken", required = true) String webToken) {
		
		JsonResult jsonResult = deviceService.query(webToken, pageIndex, pageSize, stationId);
		return jsonResult;
	}
	
	@RequestMapping("min/web/device/delete")
	public @ResponseBody JsonResult delete(HttpServletRequest request,
			@RequestParam(name ="id",  required = false) int id,
			@RequestParam(name ="webToken", required = true) String webToken) {
		
		JsonResult jsonResult = deviceService.deleted(webToken, id);
		return jsonResult;
	}
	
	@RequestMapping("min/web/device/update")
	public @ResponseBody JsonResult update(HttpServletRequest request,
			@RequestParam(name ="deviceName", required = true) String deviceName,
			@RequestParam(name ="id",  required = false) int id,
			@RequestParam(name ="webToken", required = true) String webToken) {
		
		JsonResult jsonResult = deviceService.update(webToken, id, deviceName);
		return jsonResult;
	}
}
