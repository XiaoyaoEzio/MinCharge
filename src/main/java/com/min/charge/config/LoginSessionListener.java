package com.min.charge.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.min.charge.buffer.SessionBuffer;

public class LoginSessionListener implements HttpSessionListener{

	private static final Logger logger = Logger.getLogger(LoginSessionListener.class);
	
	private SessionBuffer session = SessionBuffer.Instance;
	
	@SuppressWarnings("static-access")
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		logger.error("session创建：sessionid = " + arg0.getSession().getId());
		session.add(arg0.getSession());
		
	}

	@SuppressWarnings("static-access")
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		logger.error("session销毁：sessionid = " + arg0.getSession().getId());
		session.remove(arg0.getSession());
		
	}

}
