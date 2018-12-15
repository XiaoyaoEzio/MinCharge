package com.min.charge.schedule.job;

import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.operator.OperatorStop;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用来定时停止充电
 */
public class ChargeStopJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ChargeStopJob.class);

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        JobDataMap dataMap = jobContext.getJobDetail().getJobDataMap();
        int clientId = dataMap.getInt("clientId");
        String deviceSn = dataMap.getString("deviceSn");
        String path = dataMap.getString("path");

        logger.info("clientId：" + clientId);
        logger.info("deviceSn：" + deviceSn);
        logger.info("path：" + path);

        JsonResult stop = new OperatorStop().stop(clientId, deviceSn, path);
        if (stop.getStatus() == ErrorCodeEnum.OK.getStatus()) {
            logger.info("关闭成功");
        }
    }
}
