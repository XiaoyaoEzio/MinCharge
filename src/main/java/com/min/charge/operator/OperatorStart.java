package com.min.charge.operator;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Client;
import com.min.charge.beans.Device;
import com.min.charge.beans.OrderRecord;
import com.min.charge.beans.Price;
import com.min.charge.buffer.ChargeInfoBuffer;
import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.config.Config;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OperatorTypeEnum;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.OrderRecordMapper;
import com.min.charge.mapping.PriceMapper;
import com.min.charge.security.CommonTool;

public class OperatorStart {

	private static final Logger logger = Logger.getLogger(OperatorStart.class);
	
	public JsonResult start(Client client, String deviceSn, String path ){

		SqlSession session = MybaitsConfig.getCurrent();
		JsonResult result = new JsonResult();
		String jsonString = ChargeApi.operator(OperatorTypeEnum.Start.getCommand(), deviceSn, path);
//		jsonString = "{\"appid\":\"5203305bc0d427bf7306085a307a305e\",\"mchid\":\"921d2c2ef90c25eab42ea1f298c29822\",\"nonce_str\":\"123456\",\"sign\":\"D35B2081ACF877DB2A43762AF7772355\",\"sign_type\":\"MD5\",\"time\":\"1527481061\",\"device_id\":\"429111890496002\",\"device_path\":\"10\",\"openid\":\"oj9EwwBsPY0wJCr7mvixdJFz7IBg\",\"attach\":\"\",\"command\":\"start\"}";
		String method = "";
		Device device = DeviceBuffer.Instance.getByDeviceSn(deviceSn);
		try {
			PriceMapper priceDao = session.getMapper(PriceMapper.class);
			Price price	= priceDao.getByNow();
			if (client.getBalance() <= 0 || client.getBalance() <price.getCommonPrice()) {
				return JsonResult.code(ErrorCodeEnum.NOTENOUGH);
			}
			OrderRecord bufferRecord = ChargeInfoBuffer.Instance.getByClientId(client.getId());
			if (bufferRecord != null) {
				BillRecordsMapper billRecordDao = session.getMapper(BillRecordsMapper.class);
				BillRecords billRecord = billRecordDao.getBySn(bufferRecord.getTradingSn());
				if (billRecord == null) {
					return JsonResult.code(ErrorCodeEnum.CHARGING);
				}
				ChargeInfoBuffer.Instance.removeByClientId(client.getId());
			}
			//判断设备是否在线
			JsonResult stateResult = new OperatorState().state(client, deviceSn, path);
			if (stateResult.status != 1) {
				return stateResult;
			}
			method = HttpMethod.post(Config.Instance.device_Query_URL, jsonString);
			logger.debug("启动响应："+method);
			if (method != null || !"".equals(method)) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(method);
				int status = rootNode.get("result").asInt();
				if (status != 1) {
					logger.error(rootNode.get("val").asText());
					return JsonResult.code(ErrorCodeEnum.COMMAND_START_FAILD);
				}
				OrderRecordMapper recordDao = session.getMapper(OrderRecordMapper.class);
				logger.debug("开始记录");
				Date now = new Date();
				OrderRecord record = new OrderRecord();
				record.setClientId(client.getId());
				record.setCreatedDateTime(now);
				record.setDeviceId(device.getId());
				record.setOrderStatusEnum(OrderStatusEnum.Start);
				record.setPriceId(price.getId());
				record.setStartTime(now);
				record.setTradingSn(CommonTool.GetTradingSn(now));
				record.setTotalPauseTime(0);
				record.setPath(path);
				recordDao.save(record);
				MybaitsConfig.commitCurrent();
				ChargeInfoBuffer.Instance.addRecord(client.getId(), record);
				
			}
		} catch (Exception e) {
		
			logger.error(e.getMessage(),e);
			return JsonResult.code(ErrorCodeEnum.COMMAND_START_FAILD);
		}finally{
			MybaitsConfig.closeCurrent();
		}
		logger.debug(method);
		
		return result;
	}
	
}
