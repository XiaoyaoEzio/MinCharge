package com.min.charge.listener;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 启动和停止Quartz的监听器
 */
@Component
public class QuartzListener implements ApplicationListener {
    private static final Logger logger = Logger.getLogger(QuartzListener.class);

    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (scheduler == null) {
            return;
        }

        if (event instanceof ContextRefreshedEvent) {
            try {
                scheduler.start();
                logger.info("启动quartz");
            } catch (SchedulerException e) {
                logger.error("quartz启动异常：" + e.getMessage());
                e.printStackTrace();
            }
        } else if (event instanceof ContextClosedEvent) {
            try {
                scheduler.shutdown();
                logger.info("停止quartz");
            } catch (SchedulerException e) {
                logger.error("quartz停止异常：" + e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
