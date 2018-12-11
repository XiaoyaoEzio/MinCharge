package com.min.charge.controller;

import com.min.charge.config.MybaitsConfig;
import com.min.charge.json.JsonResult;
import com.min.charge.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RecordController {

	@Autowired
	private RecordService recordService;
	
	@RequestMapping("/min/charge/record/all")
	public @ResponseBody JsonResult getAll(HttpServletRequest request,
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
	
		JsonResult jResult = new JsonResult();
		jResult = recordService.getAll(token, pageIndex, pageSize);
		MybaitsConfig.closeCurrent();
		return jResult;
	}
	
	@RequestMapping("/min/charge/record/comsume")
	public @ResponseBody JsonResult getComsume(HttpServletRequest request,
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "id", required = true) int id,
			@RequestParam(name = "tradeType", required = true) int tradeType){
	
		JsonResult jResult = new JsonResult();
		jResult = recordService.getComsume(token, id, tradeType);
		return jResult;
	}
	
	@RequestMapping("/min/charge/record/recharge")
	public @ResponseBody JsonResult getRecharge(HttpServletRequest request,
			@RequestParam(name = "token", required = true) String token,
			@RequestParam(name = "id", required = true) int id,
			@RequestParam(name = "tradeType", required = true) int tradeType){
	
		JsonResult jResult = new JsonResult();
		jResult = recordService.getRecharge(token, id, tradeType);
		return jResult;
	}
}
