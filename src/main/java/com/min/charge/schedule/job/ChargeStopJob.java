package com.min.charge.schedule.job;

import com.min.charge.service.CommandService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用来定时停止充电
 */
@Component
public class ChargeStopJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ChargeStopJob.class);

    @Autowired
    private CommandService commandService;

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
        int clientId = dataMap.getInt("clientId");
        String deviceSn = dataMap.getString("deviceSn");
        String path = dataMap.getString("path");

        logger.info("clientId：" + clientId);
        logger.info("deviceSn：" + deviceSn);
        logger.info("path：" + path);


        commandService.stopBySystem(clientId, deviceSn, path);
        logger.info("定时关闭端口");
    }
}
