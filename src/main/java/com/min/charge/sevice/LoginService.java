package com.min.charge.sevice;

import javax.servlet.http.HttpServletRequest;

import com.min.charge.beans.Client;
import com.min.charge.json.JsonResult;

public interface LoginService {

	JsonResult dologin(HttpServletRequest request, String username,
			String image, String password, String openId);

	Client  getClietnId(int id);
	
	JsonResult register(HttpServletRequest request, String username,
			String vCode, String password, int gender);
	
	JsonResult get(HttpServletRequest request, String token);
	
	JsonResult edit(HttpServletRequest request, String token, String phoneNum, String signature, int gender);

	JsonResult doWebLogin(HttpServletRequest request, String username,
			String image, String password);
}
