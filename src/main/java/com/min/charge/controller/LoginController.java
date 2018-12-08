package com.min.charge.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.min.charge.json.JsonResult;
import com.min.charge.sevice.LoginService;

@Controller
public class LoginController {

	@Autowired
	LoginService loginService;
	
	
	@RequestMapping("/min/user/login")
	public @ResponseBody JsonResult login(HttpServletRequest request,
			@RequestParam(name ="username",required = false) String username,
			@RequestParam(name ="password",required = false) String password,
			@RequestParam(name = "openId", required =true) String openId
			) {
		JsonResult result = loginService.dologin(request, username, "image", password,openId);
		return result;
	}

	@RequestMapping("/min/user/register")
	public @ResponseBody JsonResult register(HttpServletRequest request,
			@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "openId", required = true) String openId,
			@RequestParam(name = "password", required = false) String password,
			@RequestParam(name = "gender", defaultValue = "0" ,required =false) int gender){
		
		JsonResult result = loginService.register(request, userName, openId, password, gender);
		return result;
	}
	
	@RequestMapping("/min/user/get")
	public @ResponseBody JsonResult getInfo(HttpServletRequest request,
			@RequestParam(name = "token", required = false) String token){
		
		JsonResult result = loginService.get(request, token);
		return result;
	}
	
	@RequestMapping("/min/user/edit")
	public @ResponseBody JsonResult edit(HttpServletRequest request,
			@RequestParam(name = "token", required = false) String token,
			@RequestParam(name = "gender", defaultValue = "0" ,required = false) int gender,
			@RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "phoneNum", required = false) String phoneNum){
		System.err.println(signature);
		JsonResult result = loginService.edit(request, token, phoneNum, signature, gender);
		return result;
	}
	
	@RequestMapping("/min/web/user/login")
	public @ResponseBody JsonResult webLogin(HttpServletRequest request,
			@RequestParam(name ="username",required = true) String username,
			@RequestParam(name ="password",required = true) String password) {
		JsonResult result = loginService.doWebLogin(request, username, "image", password);
		return result;
	}
	
}
