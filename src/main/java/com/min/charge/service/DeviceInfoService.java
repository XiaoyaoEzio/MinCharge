package com.min.charge.service;

import com.min.charge.json.JsonResult;

public interface DeviceInfoService {

	JsonResult getDeviceInfo(String token, String deviceSn);
}
