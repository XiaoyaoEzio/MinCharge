package com.min.charge.web.service;


import com.min.charge.json.JsonResult;

public interface StationService {
	
	JsonResult save(String webToken, String stationName, float latitude, float longitude, String provinceName, String provinceCode, String cityName, String cityCode, String streetName);
	
	JsonResult query(String webToken, int pageIndex, int pageSize, String stationName);
	
	JsonResult update(String webToken, int id, String stationName, float latitude,
			float longitude, String provinceName, String provinceCode,
			String cityName, String cityCode, String streetName);
	
}
