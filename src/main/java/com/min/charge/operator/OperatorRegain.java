package com.min.charge.operator;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.Client;
import com.min.charge.beans.OrderRecord;
import com.min.charge.buffer.ChargeInfoBuffer;
import com.min.charge.config.Config;
import com.min.charge.config.MybatisConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OperatorTypeEnum;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.OrderRecordMapper;

public class OperatorRegain {

private static final Logger logger = Logger.getLogger(OperatorRegain.class);
	
	public JsonResult regain(Client client,String deviceSn, String path){
		JsonResult result = new JsonResult();
		SqlSession session = MybatisConfig.getCurrent();
		String jsonString = ChargeApi.operator(OperatorTypeEnum.Regain.getCommand(), deviceSn,  path);
		String method = "";
		try {
			OrderRecord bufferRecord = ChargeInfoBuffer.Instance.getByClientId(client.getId());
			if (bufferRecord == null  ) {
				return JsonResult.code(ErrorCodeEnum.NO_CHARGING);
			}
			method =  HttpMethod.post(Config.Instance.device_Query_URL, jsonString);
			if (method != null || !"".equals(method)) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(method);
				int status = rootNode.get("result").asInt();
				if (status != 1) {
					logger.error(rootNode.get("val").asText());
					return JsonResult.code(ErrorCodeEnum.COMMAND_REGAIN_FAILD);
				}
				Date now = new Date();
				int totalPause = (int)(now.getTime() - bufferRecord.getLastPauseTime().getTime());
				bufferRecord.setOrderStatusEnum(OrderStatusEnum.Start);
				bufferRecord.setTotalPauseTime(totalPause);
				OrderRecordMapper oRecordMapper = session.getMapper(OrderRecordMapper.class);
				oRecordMapper.update(bufferRecord);
				MybatisConfig.commitCurrent();
				ChargeInfoBuffer.Instance.addRecord(client.getId(), bufferRecord);
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}finally{
			MybatisConfig.closeCurrent();
		}
		
		return result;
	}
}
