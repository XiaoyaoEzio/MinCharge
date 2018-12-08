package com.min.charge.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min.charge.json.JsonResult;
import com.min.charge.sevice.CommandService;

@Controller
public class CommandController {
	
	@Autowired
	CommandService commandService;
	
	@RequestMapping("/min/charge/command/start")
	public @ResponseBody JsonResult start(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceId,
			@RequestParam(name = "path", required = true) String  path) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult = commandService.start(request,token, deviceId, path);
		return jsonResult;
	}
	
	@RequestMapping("/min/charge/connectcd")
	public @ResponseBody JsonResult connect(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceSn) {
		
		JsonResult jsonResult = new JsonResult();
		commandService.connect(request,token, deviceSn);
		return jsonResult;
	}
	
	@RequestMapping("/min/charge/command/pause")
	public @ResponseBody JsonResult pause(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceId,
			@RequestParam(name = "path", required = false) String  path) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult = commandService.pause(request,token, deviceId, path);
		return jsonResult;
	}
	
	@RequestMapping("/min/charge/command/stop")
	public @ResponseBody JsonResult stop(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceId,
			@RequestParam(name = "path", required = false) String  path) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult = commandService.stop(request,token, deviceId, path);
		return jsonResult;
	}
	
	@RequestMapping("/min/charge/command/regain")
	public @ResponseBody JsonResult regain(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceId,
			@RequestParam(name = "path", required = false) String  path) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult = commandService.regain(request,token, deviceId, path);
		return jsonResult;
	}
	
	@RequestMapping("/min/charge/command/state")
	public @ResponseBody JsonResult state(HttpServletRequest request, 
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "deviceSn", required = true) String  deviceId,
			@RequestParam(name = "path", required = false) String  path) {
		
		JsonResult jsonResult = new JsonResult();
		jsonResult = commandService.state(request,token, deviceId, path);
		return jsonResult;
	}

}
