package com.min.charge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min.charge.json.JsonResult;
import com.min.charge.web.service.PriceService;

@Controller
public class PriceController {

	@Autowired
	PriceService priceService;
	
	
	@RequestMapping("min/web/price/save")
	public @ResponseBody JsonResult save(HttpServletRequest request,
			@RequestParam(name ="priceValue", required = true) int priceValue,
			@RequestParam(name ="webToken", required = true) String webToken) {
		 return priceService.save(webToken, priceValue);
	}
	
	@RequestMapping("min/web/price/current")
	public @ResponseBody JsonResult query(HttpServletRequest request,
			@RequestParam(name ="webToken", required = true) String webToken) {
		 return priceService.current(webToken);
	}
	
	@RequestMapping("min/web/price/update")
	public @ResponseBody JsonResult update(HttpServletRequest request,
			@RequestParam(name ="priceValue", required = true) int priceValue,
			@RequestParam(name ="webToken", required = true) String webToken) {
		 return priceService.update(webToken,priceValue);
	}
}
