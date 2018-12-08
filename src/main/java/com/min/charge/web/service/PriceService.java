package com.min.charge.web.service;

import com.min.charge.json.JsonResult;

public interface PriceService {

	JsonResult save(String webToken, int priceValue);
	
	JsonResult current(String webToken);
	
	JsonResult update(String webToken, int priceValue);
}
