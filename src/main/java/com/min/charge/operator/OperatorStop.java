package com.min.charge.operator;

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
import com.min.charge.config.MybatisConfig;
import com.min.charge.enums.*;
import com.min.charge.http.HttpMethod;
import com.min.charge.http.api.ChargeApi;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.OrderRecordMapper;
import com.min.charge.mapping.PriceMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperatorStop {

	private static final Logger logger = Logger.getLogger(OperatorStop.class);

	private static final Object lock = new Object();

	public JsonResult stop(int clientId, String deviceSn, String path) {

		JsonResult result = new JsonResult();
		String jsonString = ChargeApi.operator(
				OperatorTypeEnum.Stop.getCommand(), deviceSn, path);
		String method = "";
		SqlSession session = MybatisConfig.getCurrent();
		String bufferSn = "";
		try {
			OrderRecord bufferRecord = ChargeInfoBuffer.Instance.getByClientId(clientId);
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
			method = HttpMethod.post(Config.Instance.device_Query_URL, jsonString);
			if (method != null || !"".equals(method)) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(method);
				int status = rootNode.get("result").asInt();
				if (status != 1) {
					logger.error(rootNode.get("val").asText());
					return JsonResult.code(ErrorCodeEnum.COMMAND_START_FAILD);
				}
				OrderRecordMapper orderDao = session.getMapper(OrderRecordMapper.class);
				BillRecordsMapper billDao = session.getMapper(BillRecordsMapper.class);
				PriceMapper priceDao = session.getMapper(PriceMapper.class);
				ClientMapper clientDao = session.getMapper(ClientMapper.class);
				Client client = clientDao.getById(clientId);
				Date stopDate = new Date();
				Price price = priceDao.getByNow();

				// 判断当前状态是否为暂停
				int pause = bufferRecord.getOrderStatusEnum().compareTo(OrderStatusEnum.Pause);

                long startTime = bufferRecord.getStartTime().getTime();
                long stopTime = stopDate.getTime();
                Integer totalPauseTime = bufferRecord.getTotalPauseTime();
                if (totalPauseTime == null) {
                    totalPauseTime = 0;
                }

                long chargeTime;
                if (pause == 0) {
                    long lastPauseTime = bufferRecord.getLastPauseTime().getTime();
                    chargeTime = lastPauseTime - startTime;
                    // 更新暂停时长
                    int newPauseTime = new Long(stopTime - lastPauseTime).intValue();
                    bufferRecord.setTotalPauseTime(totalPauseTime + newPauseTime);
                } else {
                    chargeTime = stopTime - startTime;
                }

                // chargeTime 总时长，costTime 需要支付的时长
				long costTime = chargeTime - totalPauseTime;

				// 需付费的充电时长向上取整
				int costMin = (int) ((costTime / (1000 * 60)) + 1);

				// TODO 暂时将花费设置为0
				//int cost = price.getCommonPrice() / 60 * costMin;
                int cost = 0;

				BillRecords bill = new BillRecords();
				bill.setClientId(clientId);
				bill.setCreatedDateTime(stopDate);
				bill.setDeviceId(DeviceBuffer.Instance.getByDeviceSn(deviceSn).getId());
				bill.setPriceId(price.getId());
				bill.setTotalFee(cost);
				bill.setTradeStatusEnum(TradeStatusEnum.Finished);
				bill.setTradeType(TradeTypeEnum.COMSUME);
				bill.setTradingSn(bufferRecord.getTradingSn());

				bufferRecord.setOrderStatusEnum(OrderStatusEnum.Stop);
				bufferRecord.setStopTime(stopDate);
				/*if (client.getBalance() < cost) {
					clientDao.updateBalance(clientId, -client.getBalance());
				} else {
					clientDao.updateBalance(clientId, -cost);
				}*/
                clientDao.updateBalance(clientId, -cost);

				ChargeInfoBuffer.Instance.removeByTradeSn(bufferRecord.getTradingSn());
				ChargeInfoBuffer.Instance.removeByDeviceId_Path(bufferRecord.getDeviceId(),bufferRecord.getPath());
				orderDao.update(bufferRecord);
				billDao.save(bill);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				PayRecord payRecord = new PayRecord();
				payRecord.startTime = dateFormat.format(bufferRecord.getStartTime());
				payRecord.endTime = dateFormat.format(bufferRecord.getStopTime());
				payRecord.deviceSn = deviceSn;
				payRecord.totalFee = bill.getTotalFee();
				payRecord.totalPauseTime = totalPauseTime;
				payRecord.tradingSn = bufferRecord.getTradingSn();
				result = JsonResult.data(payRecord);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			session.rollback();
			return JsonResult.code(ErrorCodeEnum.COMMAND_STOP_FAILD);
		} finally {
			MybatisConfig.commitCurrent();
			MybatisConfig.closeCurrent();
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
