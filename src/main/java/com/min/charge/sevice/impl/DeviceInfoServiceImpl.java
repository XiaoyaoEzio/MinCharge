package com.min.charge.sevice.impl;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.Device;
import com.min.charge.beans.Price;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OperatorTypeEnum;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;
import com.min.charge.json.PathState;
import com.min.charge.mapping.DeviceMapper;
import com.min.charge.mapping.PriceMapper;
import com.min.charge.sevice.DeviceInfoService;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService{
	
	private static final Logger logger = Logger.getLogger(DeviceInfoServiceImpl.class);
	
	@Autowired
	private DeviceMapper deviceMapper;

	@Autowired
	private PriceMapper priceMapper;
	
	@Override
	public JsonResult getDeviceInfo(String token, String deviceSn) {
		
		Device device = deviceMapper.getByDeviceSn(deviceSn);
		if (device == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		Price price = priceMapper.getByNow();
		String jsonString = ChargeApi.operator(OperatorTypeEnum.State.getCommand(), deviceSn,"01");
//		jsonString = "{\"appid\":\"5203305bc0d427bf7306085a307a305e\",\"mchid\":\"921d2c2ef90c25eab42ea1f298c29822\",\"nonce_str\":\"123456\",\"sign\":\"78D6E8F5BCB3A4F63665A572DB057C79\",\"sign_type\":\"MD5\",\"time\":\"1527481061\",\"device_id\":\"429111890496002\",\"device_path\":\"01\",\"openid\":\"oj9EwwBsPY0wJCr7mvixdJFz7IBg\",\"attach\":\"\",\"command\":\"state\"}";
		String method = "";
		DeviceInfo deviceInfo = new DeviceInfo();
		try {
			method = HttpMethod.post("http://api.cdxiongmaoyun.com/CloudPandaAPI/Command", jsonString);
//			method = "{\"result\":1,\"val\":{\"message\":\"success\",\"req\":{\"appid\":\"5203305bc0d427bf7306085a307a305e\",\"mchid\":\"921d2c2ef90c25eab42ea1f298c29822\",\"nonce_str\":\"123456\",\"sign\":\"78D6E8F5BCB3A4F63665A572DB057C79\",\"sign_type\":\"MD5\",\"time\":\"1527481061\",\"device_id\":\"429111890496002\",\"device_path\":\"01\",\"openid\":\"oj9EwwBsPY0wJCr7mvixdJFz7IBg\",\"attach\":\"\",\"command\":\"state\"},\"online\":1,\"path_state\":[{\"path\":\"01\",\"state\":\"2\",\"power\":\"1500\"},{\"path\":\"02\",\"state\":\"1\",\"power\":\"0220\"}]}}";
			logger.debug("返回信息：" + method);
			String response = method.replaceAll("\\\\","");
			logger.debug("返回信息：" + response);
			if (method != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
				JsonNode rootNode = objectMapper.readTree(response);
				int status = rootNode.get("result").asInt();
				logger.error(rootNode.get("val").asText());
				if (status != 1) {
					return JsonResult.code(ErrorCodeEnum.COMMAND_UNKNOWN);
				}
				logger.debug("fd: "+ rootNode.get("val").asText());
				JsonNode valNode = rootNode.get("val");
//				LinkedList<PathState> pathStates = new LinkedList<PathState>();
				deviceInfo.online = valNode.get("online").asInt();
				if (deviceInfo.online != 1) {
					return JsonResult.code(ErrorCodeEnum.OFFLINE);
				}
				deviceInfo.deviceName = device.getDeviceName();
				deviceInfo.deviceSn = deviceSn;
//				deviceInfo.path_state = StringEncoderComparator.;
				JsonNode pathNode = valNode.get("path_state");
				Collection<PathState> pathStates = new LinkedList<PathState>();
				for (JsonNode jsonNode : pathNode) {
					if (!jsonNode.isNull()) {
						PathState pathState = new PathState();
						pathState.setPath(jsonNode.get("path").asText());
						pathState.setPower(jsonNode.get("power").asText());
						pathState.setState(jsonNode.get("state").asText());
						pathStates.add(pathState);
					}
				}
				deviceInfo.path_state = pathStates;
				deviceInfo.price = price.getCommonPrice();
				deviceInfo.pathTotal = pathStates.size();
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}
		return JsonResult.data(deviceInfo);
	}
	
	class DeviceInfo{
		public String deviceSn;
		
		public String deviceName;
		
		public int online;
		
		public int pathTotal;
		
		public Collection<PathState> path_state;
		
		public int price;
		
	}

}
