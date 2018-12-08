package com.min.charge.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min.charge.json.JsonResult;
import com.min.charge.web.service.StationService;

@Controller
public class StationController {
	
	@Autowired
	StationService stationService;

	@RequestMapping("min/web/station/save")
	@ResponseBody
	public  JsonResult save(HttpServletRequest request,
			@RequestParam(name ="webToken", required = true) String webToken,
			@RequestParam(name = "stationName", required = true) String stationName,
			@RequestParam(name = "latitude", required = true) float latitude,
			@RequestParam(name = "longitude", required = true) float longitude,
			@RequestParam(name = "provinceName", required = true) String provinceName,
			@RequestParam(name = "provinceCode", required = true) String provinceCode,
			@RequestParam(name = "cityName", required = true) String cityName,
			@RequestParam(name = "cityCode", required = true) String cityCode,
			@RequestParam(name = "streetName", required = true) String streetName){
		
		JsonResult result = stationService.save(webToken,stationName, latitude, longitude, provinceName, provinceCode, cityName, cityCode, streetName);
		return result;
	}
	
	@RequestMapping("min/web/station/query")
	@ResponseBody
	public JsonResult qurey(HttpServletRequest request,
			@RequestParam(name ="webToken", required = true) String webToken,
			@RequestParam(name = "stationName", required = false) String stationName,
			@RequestParam(name = "pageIndex", required = true , defaultValue = "1") int pageIndex,
			@RequestParam(name = "pageSize", required = true , defaultValue = "20") int pageSize){
		JsonResult result = stationService.query(webToken, pageIndex, pageSize, stationName);
		return result;
	}
	
	@RequestMapping("min/web/station/update")
	@ResponseBody
	public  JsonResult update(HttpServletRequest request,
			@RequestParam(name ="webToken", required = true) String webToken,
			@RequestParam(name="id", required = true) int id,
			@RequestParam(name = "stationName", required = false) String stationName,
			@RequestParam(name = "latitude", required = false, defaultValue ="0") float latitude,
			@RequestParam(name = "longitude", required = false, defaultValue ="0") float longitude,
			@RequestParam(name = "provinceName", required = false) String provinceName,
			@RequestParam(name = "provinceCode", required = false) String provinceCode,
			@RequestParam(name = "cityName", required = false) String cityName,
			@RequestParam(name = "cityCode", required = false) String cityCode,
			@RequestParam(name = "streetName", required = false) String streetName){
		
		JsonResult result = stationService.update(webToken,id,stationName, latitude, longitude, provinceName, provinceCode, cityName, cityCode, streetName);
		return result;
	}
}
