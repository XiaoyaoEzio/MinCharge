package com.min.charge.operator;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.Client;
import com.min.charge.config.Config;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OperatorTypeEnum;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;

public class OperatorState {

	private static final Logger logger = Logger.getLogger(OperatorState.class);
	
	public JsonResult state(Client client, String deviceSn, String path){
		
		String jsonString = ChargeApi.operator(OperatorTypeEnum.State.getCommand(), deviceSn, path);
//		jsonString = "{\"appid\":\"5203305bc0d427bf7306085a307a305e\",\"mchid\":\"921d2c2ef90c25eab42ea1f298c29822\",\"nonce_str\":\"123456\",\"sign\":\"78D6E8F5BCB3A4F63665A572DB057C79\",\"sign_type\":\"MD5\",\"time\":\"1527481061\",\"device_id\":\"429111890496002\",\"device_path\":\"01\",\"openid\":\"oj9EwwBsPY0wJCr7mvixdJFz7IBg\",\"attach\":\"\",\"command\":\"state\"}";
		String method = "";
		logger.debug(jsonString);
		try {
			method = HttpMethod.post(Config.Instance.device_Query_URL, jsonString);
			
			logger.debug("method: "+method);
			if (method != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(method);
				int status = rootNode.get("result").asInt();
				if (status != 1) {
					logger.error(rootNode.get("val").asText());
					return JsonResult.code(ErrorCodeEnum.COMMAND_UNKNOWN);
				}
				JsonNode valNode =rootNode.get("val");
				int deviceState = valNode.get("online").asInt();
				if (deviceState != 1)
					return JsonResult.code(ErrorCodeEnum.OFFLINE);
				
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return JsonResult.code(ErrorCodeEnum.ONLINE);
	}
}
