package com.min.charge.service.impl;

import com.min.charge.beans.Client;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.enums.ChargeRankEnum;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.operator.*;
import com.min.charge.schedule.job.ChargeStopJob;
import com.min.charge.service.CommandService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class CommandServiceImpl implements CommandService {
    private static final Logger logger = Logger.getLogger(CommandServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public JsonResult connect(HttpServletRequest request, String token,
                              String deviceSn) {

        return null;
    }

    class ConnectInfo {

        public String deviceId;

        public int state;


    }

    @Override
    public JsonResult start(HttpServletRequest request, String token,
                            String deviceSn, String path, String chargeRank) {
        System.out.println("chargeRank: " + chargeRank);

        // TODO 暂时不验证登录
        /*Client client = LoginBuffer.getClient(token);
        if (client == null) {
            return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
        }*/

        Client client = clientMapper.getById(70);

        // 设置定时关闭
        int chargeTime = ChargeRankEnum.getTimeByRank(Integer.parseInt(chargeRank));
        if (chargeTime == -1) {
            return JsonResult.code(ErrorCodeEnum.CHARGE_RANK_ERROR);
        }

        JsonResult start = new OperatorStart().start(client, deviceSn, path);
        if (start.getStatus() != ErrorCodeEnum.OK.getStatus()) {
            return start;
        }

        boolean result = setStopTime(chargeTime, client.getId(), deviceSn, path);
        if (result) {
            return start;
        }
        else {
            return JsonResult.code(ErrorCodeEnum.SET_STOP_TIME_FAILD);
        }

    }

    @Override
    public JsonResult pause(HttpServletRequest request, String token,
                            String deviceSn, String path) {
        Client client = LoginBuffer.getClient(token);
        if (client == null) {
            return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult = new OperatorPause().pause(client, deviceSn, path);
        return jsonResult;
    }

    @Override
    public JsonResult regain(HttpServletRequest request, String token,
                             String deviceSn, String path) {
        Client client = LoginBuffer.getClient(token);
        if (client == null) {
            return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult = new OperatorRegain().regain(client, deviceSn, path);
        return jsonResult;
    }

    @Override
    public JsonResult state(HttpServletRequest request, String token,
                            String deviceSn, String path) {
        Client client = LoginBuffer.getClient(token);
        if (client == null) {
            return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
        }
        JsonResult jsonResult = new JsonResult();

        jsonResult = new OperatorState().state(client, deviceSn, path);
        return jsonResult;
    }

    @Override
    public JsonResult stop(HttpServletRequest request, String token,
                           String deviceSn, String path) {
        Client client = LoginBuffer.getClient(token);
        if (client == null) {
            return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
        }
        JsonResult jsonResult = new JsonResult();
        jsonResult = new OperatorStop().stop(client.getId(), deviceSn, path);
        return jsonResult;
    }

    // 设置定时关闭时间
    private boolean setStopTime(int chargeTime, int clientId, String deviceSn, String path) {
        // 自动充满不设置定时关闭
        if (chargeTime == ChargeRankEnum.AUTO.getTime()) {
            return true;
        }

        String baseName = clientId + new Date().getTime() +"";
        String jobName = "job" + baseName;
        String groupName = "group" + baseName;
        String triggerName = "trigger" + baseName;

        JobDetail jobDetail = JobBuilder.newJob(ChargeStopJob.class)
                .withIdentity(jobName, groupName)
                .usingJobData("clientId", clientId)
                .usingJobData("deviceSn", deviceSn)
                .usingJobData("path", path)
                .build();

        // TODO 暂时将充电时间设置为10秒
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, groupName)
                .startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.SECOND))
                .forJob(jobName, groupName)
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            logger.error("quartz异常：" + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
