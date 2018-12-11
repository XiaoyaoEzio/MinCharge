package com.min.charge.controller;

import com.min.charge.service.MicroNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MicroNotifyController {

	@Autowired
	MicroNotifyService microNotifyService;
	
	@RequestMapping("/min/micropay/notify")
	public void notify(HttpServletRequest request, HttpServletResponse response){
		microNotifyService.notifyPay(request, response);
	}
}
