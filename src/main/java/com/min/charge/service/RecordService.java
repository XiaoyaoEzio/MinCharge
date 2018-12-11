package com.min.charge.service;

import com.min.charge.json.JsonResult;

public interface RecordService {
	
	JsonResult getAll(String token, int pageIndex, int pageSize);
	
	JsonResult getComsume(String token, int id, int tradeType);
	
	JsonResult getRecharge(String token, int id, int tradeType);
}
