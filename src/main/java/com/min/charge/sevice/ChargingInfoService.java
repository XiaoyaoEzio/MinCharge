package com.min.charge.sevice;

import com.min.charge.json.JsonResult;

public interface ChargingInfoService {

	JsonResult refresh(String token, String deviceSn);
}
