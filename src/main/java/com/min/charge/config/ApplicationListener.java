package com.min.charge.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.buffer.data.ThreadOffline;

public class ApplicationListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Config.Instance.loadConfig();
		DeviceBuffer.Instance.init();
		ThreadOffline.Instance.start();
	}

}
