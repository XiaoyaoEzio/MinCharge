package com.min.charge.buffer.data;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Client;
import com.min.charge.beans.Device;
import com.min.charge.beans.OrderRecord;
import com.min.charge.beans.Price;
import com.min.charge.buffer.ChargeInfoBuffer;
import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.enums.TradeTypeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.OrderRecordMapper;
import com.min.charge.mapping.PriceMapper;
import com.min.charge.operator.OperatorState;

public class DataChargeInfo {
	
	private static final Logger logger = Logger.getLogger(DataChargeInfo.class);
	
	public void refresh() {
		Map<String, Device> bufferMap = DeviceBuffer.Instance.getDeviceMap();
		logger.debug("轮询大小"+bufferMap.size());
		SqlSession session = MybaitsConfig.getCurrent();
		OrderRecordMapper orderDao = session
				.getMapper(OrderRecordMapper.class);
		for (String item : bufferMap.keySet()) {
			JsonResult jsonResult = new OperatorState().state(null, item, "01");
			Device device = bufferMap.get(item);
			device.setDeviceStatus(jsonResult.status);
			bufferMap.put(device.getDeviceSn(), device);
			if (jsonResult.status == 2) {
				logger.error("设备"+device.getDeviceName()+"序号为："+device.getDeviceSn()+"离线");
				
				try {
					Collection<OrderRecord> buffers = orderDao.getByDeviceId_Working(device.getId());
					if (buffers != null && buffers.size()>0) {
						for (OrderRecord buffer : buffers) {
						
						BillRecordsMapper billDao = session
								.getMapper(BillRecordsMapper.class);
						PriceMapper priceDao = session.getMapper(PriceMapper.class);
						ClientMapper clientDao = session.getMapper(ClientMapper.class);
						Client client = clientDao.getById(buffer.getClientId());
						Date stopTime = new Date();
						Price price = priceDao.getByNow();
						
						int pause = buffer.getOrderStatusEnum().compareTo(
								OrderStatusEnum.Pause);
						long chargeTime =  ((pause == 0 ? buffer
								.getLastPauseTime().getTime() : stopTime.getTime()) - buffer
								.getStartTime().getTime());
						logger.debug("结束时间："+(pause == 0 ? buffer
								.getLastPauseTime().getTime() : stopTime.getTime()));
						logger.debug("开始时间："+buffer
								.getStartTime().getTime());
						long costTime = chargeTime
								- (buffer.getTotalPauseTime() == null ? 0
										: buffer.getTotalPauseTime());
						if (pause == 0) {
							int totalPauseTime = (int)(stopTime.getTime() - buffer.getLastPauseTime().getTime());
							buffer.setTotalPauseTime(buffer.getTotalPauseTime()+(int)totalPauseTime);	
						}
						int costMin = (int)(costTime / (1000 * 60));
						int cost = price.getCommonPrice() * costMin;
						BillRecords bill = new BillRecords();
						bill.setClientId(client.getId());
						bill.setCreatedDateTime(stopTime);
						bill.setDeviceId(DeviceBuffer.Instance.getByDeviceSn(item)
								.getId());
						bill.setPriceId(price.getId());
						bill.setTotalFee(cost);
						bill.setTradeStatusEnum(TradeStatusEnum.Finished);
						bill.setTradeType(TradeTypeEnum.COMSUME);
						bill.setTradingSn(buffer.getTradingSn());
						
						buffer.setOrderStatusEnum(OrderStatusEnum.Stop);
						buffer.setStopTime(stopTime);
						if (client.getBalance() < cost) {
							clientDao.updateBalance(client.getId(),
									-client.getBalance());
						} else {
							clientDao.updateBalance(client.getId(), -cost);
						}
						ChargeInfoBuffer.Instance.removeByTradeSn(buffer
								.getTradingSn());
						ChargeInfoBuffer.Instance.removeByDeviceId_Path(buffer.getDeviceId(),buffer.getPath());
						orderDao.update(buffer);
						billDao.save(bill);
					}
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
		MybaitsConfig.commitCurrent();
		MybaitsConfig.closeCurrent();
	}
}
