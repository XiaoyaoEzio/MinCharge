package com.min.charge.web.service;

import com.min.charge.json.JsonResult;

public interface DeviceService {

	JsonResult save(String webToken, String deviceSn, String deviceName,int stationId);
	
	JsonResult query(String webToken, int pageIndex, int pageSize, int stationId);
	
	JsonResult deleted(String webToken, int id);
	
	JsonResult update(String webToken, int id, String deviceName);
}
