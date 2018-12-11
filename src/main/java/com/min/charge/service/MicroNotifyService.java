package com.min.charge.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface MicroNotifyService {
	
	void notifyPay(HttpServletRequest request, HttpServletResponse response);
}
