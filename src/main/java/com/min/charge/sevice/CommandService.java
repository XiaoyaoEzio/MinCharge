package com.min.charge.sevice;

import javax.servlet.http.HttpServletRequest;

import com.min.charge.json.JsonResult;

public interface CommandService {

	JsonResult connect(HttpServletRequest request, String token, String deviceSn);
	
	JsonResult start(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult pause(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult regain(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult state(HttpServletRequest request, String token, String deviceSn, String path);

	JsonResult stop(HttpServletRequest request, String token, String deviceSn, String path);

}
