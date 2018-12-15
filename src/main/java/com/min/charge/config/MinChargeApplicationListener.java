package com.min.charge.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MinChargeApplicationListener implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        /*Config.Instance.loadConfig();
		DeviceBuffer.Instance.init();
		ThreadOffline.Instance.start();*/
	}
}
