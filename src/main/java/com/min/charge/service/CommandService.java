package com.min.charge.service;

import com.min.charge.json.JsonResult;

import javax.servlet.http.HttpServletRequest;

public interface CommandService {

	JsonResult connect(HttpServletRequest request, String token, String deviceSn);
	
	JsonResult start(HttpServletRequest request, String token, String deviceSn, String path, String chargeRank);

	JsonResult pause(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult regain(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult state(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult stop(HttpServletRequest request, String token, String deviceSn, String path);

	void stopBySystem(int clientId, String deviceSn, String path);
}
