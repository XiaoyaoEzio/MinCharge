package com.min.charge.operator;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Client;
import com.min.charge.beans.OrderRecord;
import com.min.charge.beans.Price;
import com.min.charge.buffer.ChargeInfoBuffer;
import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.buffer.TradingSnBuffer;
import com.min.charge.config.Config;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OperatorTypeEnum;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.enums.TradeTypeEnum;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.OrderRecordMapper;
import com.min.charge.mapping.PriceMapper;

public class OperatorStop {

	private static final Logger logger = Logger.getLogger(OperatorStop.class);

	private static final Object lock = new Object();

	public JsonResult stop(Client client, String deviceSn, String path) {

		JsonResult result = new JsonResult();
		String jsonString = ChargeApi.operator(
				OperatorTypeEnum.Stop.getCommand(), deviceSn, path);
		String method = "";
		SqlSession session = MybaitsConfig.getCurrent();
		String bufferSn = "";
		try {
			OrderRecord bufferRecord = ChargeInfoBuffer.Instance
					.getByClientId(client.getId());
			if (bufferRecord == null) {
				return JsonResult.code(ErrorCodeEnum.NO_CHARGING);
			}
			bufferSn = bufferRecord.getTradingSn();
			synchronized (lock) {
				if (TradingSnBuffer.Instance.get(bufferRecord.getTradingSn())) {
					return JsonResult.code(ErrorCodeEnum.DEALING);
				}
				TradingSnBuffer.Instance.put(bufferRecord.getTradingSn());
			}
			method = HttpMethod.post(
					Config.Instance.device_Query_URL,
					jsonString);
			if (method != null || !"".equals(method)) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(method);
				int status = rootNode.get("result").asInt();
				if (status != 1) {
					logger.error(rootNode.get("val").asText());
					return JsonResult.code(ErrorCodeEnum.COMMAND_START_FAILD);
				}
				OrderRecordMapper orderDao = session
						.getMapper(OrderRecordMapper.class);
				BillRecordsMapper billDao = session
						.getMapper(BillRecordsMapper.class);
				PriceMapper priceDao = session.getMapper(PriceMapper.class);
				ClientMapper clientDao = session.getMapper(ClientMapper.class);
				client = clientDao.getById(client.getId());
				Date stopTime = new Date();
				Price price = priceDao.getByNow();
				
				int pause = bufferRecord.getOrderStatusEnum().compareTo(
						OrderStatusEnum.Pause);
				long chargeTime =  ((pause == 0 ? bufferRecord
						.getLastPauseTime().getTime() : stopTime.getTime()) - bufferRecord
						.getStartTime().getTime());
				long costTime = chargeTime
						- (bufferRecord.getTotalPauseTime() == null ? 0
								: bufferRecord.getTotalPauseTime());
				if (pause == 0) {
					int totalPauseTime = (int)(stopTime.getTime() - bufferRecord.getLastPauseTime().getTime());
					bufferRecord.setTotalPauseTime(bufferRecord.getTotalPauseTime()+(int)totalPauseTime);	
				}
				int costMin = (int) (costTime / (1000 * 60));
				int cost = price.getCommonPrice() * costMin;
				BillRecords bill = new BillRecords();
				bill.setClientId(client.getId());
				bill.setCreatedDateTime(stopTime);
				bill.setDeviceId(DeviceBuffer.Instance.getByDeviceSn(deviceSn)
						.getId());
				bill.setPriceId(price.getId());
				bill.setTotalFee(cost);
				bill.setTradeStatusEnum(TradeStatusEnum.Finished);
				bill.setTradeType(TradeTypeEnum.COMSUME);
				bill.setTradingSn(bufferRecord.getTradingSn());

				bufferRecord.setOrderStatusEnum(OrderStatusEnum.Stop);
				bufferRecord.setStopTime(stopTime);
				if (client.getBalance() < cost) {
					clientDao.updateBalance(client.getId(),
							-client.getBalance());
				} else {
					clientDao.updateBalance(client.getId(), -cost);
				}
				ChargeInfoBuffer.Instance.removeByTradeSn(bufferRecord
						.getTradingSn());
				ChargeInfoBuffer.Instance.removeByDeviceId_Path(bufferRecord.getDeviceId(),bufferRecord.getPath());
				orderDao.update(bufferRecord);
				billDao.save(bill);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				PayRecord payRecord = new PayRecord();
				payRecord.startTime = dateFormat.format(bufferRecord
						.getStartTime());
				payRecord.endTime = dateFormat.format(bufferRecord
						.getStopTime());
				payRecord.deviceSn = deviceSn;
				payRecord.totalFee = bill.getTotalFee();
				payRecord.totalPauseTime = bufferRecord.getTotalPauseTime();
				payRecord.tradingSn = bufferRecord.getTradingSn();
				result = JsonResult.data(payRecord);

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.rollback();
			return JsonResult.code(ErrorCodeEnum.COMMAND_STOP_FAILD);
		} finally {
			MybaitsConfig.commitCurrent();
			MybaitsConfig.closeCurrent();
			TradingSnBuffer.Instance.remove(bufferSn);
		}

		return result;
	}

	class PayRecord {
		public String startTime;

		public String endTime;

		public int totalPauseTime;

		public String tradingSn;

		public int totalFee;

		public String deviceSn;

	}
}
