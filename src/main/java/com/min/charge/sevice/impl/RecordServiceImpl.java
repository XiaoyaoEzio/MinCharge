package com.min.charge.sevice.impl;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.min.charge.beans.BillRecords;
import com.min.charge.beans.Client;
import com.min.charge.beans.Device;
import com.min.charge.beans.OrderRecord;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.TradeTypeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.BillRecordsMapper;
import com.min.charge.mapping.DeviceMapper;
import com.min.charge.mapping.OrderRecordMapper;
import com.min.charge.sevice.RecordService;

@Service
public class RecordServiceImpl implements RecordService{

	private static final Logger logger = Logger.getLogger(RecordServiceImpl.class);
	
	@Override
	public JsonResult getAll(String token, int pageIndex, int pageSize) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession sqlSession = MybaitsConfig.getCurrent();
		BillRecordsMapper billRecordsDao = sqlSession.getMapper(BillRecordsMapper.class);
		Collection<BillRecords> recordHistorys = billRecordsDao.getAll(client.getId(), (pageIndex-1)*pageSize, pageSize*pageIndex);
		Collection<RecordInfo> records = new LinkedList<RecordServiceImpl.RecordInfo>();
		RecordInfos infos = new RecordInfos();
		infos.pageIndex = pageIndex;
		infos.pageSize = pageSize;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BillRecords item : recordHistorys) {
			RecordInfo info = new RecordInfo();
			info.id = item.getId();
			info.tradeType = item.getTradeType().getValue();
			info.tradeStatusEnum = item.getTradeStatusEnum().getValue();
			info.tradingSn = item.getTradingSn();
			info.tradeTime = dateFormat.format(item.getCreatedDateTime());
			info.totalFee = item.getTotalFee();
			records.add(info);
		}
		infos.records = records;
		MybaitsConfig.closeCurrent();
		return JsonResult.data(infos);
		
	}

	@Override
	public JsonResult getComsume(String token, int id, int tradeType) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		if (TradeTypeEnum.COMSUME.getValue() != tradeType) {
			logger.error("非消费类型账单");
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		SqlSession sqlSession = MybaitsConfig.getCurrent();
		BillRecordsMapper billRecordsDao = sqlSession.getMapper(BillRecordsMapper.class);
		OrderRecordMapper orderRecordDao = sqlSession.getMapper(OrderRecordMapper.class);
		DeviceMapper deviceDao = sqlSession.getMapper(DeviceMapper.class);
		BillRecords billRecords = billRecordsDao.getById(id);
		if (billRecords == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		OrderRecord orderRecord = orderRecordDao.getByTradeSn(billRecords.getTradingSn());
		if (orderRecord == null) {
			logger.error("消费订单不存在");
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);	
		}
		Device device = deviceDao.getById(billRecords.getDeviceId());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ComsumeDetail comsumeDetail = new ComsumeDetail();
		comsumeDetail.id = billRecords.getId();
		comsumeDetail.tradeType = billRecords.getTradeType().getValue();
		comsumeDetail.tradeStatusEnum = billRecords.getTradeStatusEnum().getValue();
		comsumeDetail.tradingSn = billRecords.getTradingSn();
		comsumeDetail.tradeTime = dateFormat.format(billRecords.getCreatedDateTime());
		comsumeDetail.totalFee = billRecords.getTotalFee();
		comsumeDetail.startTime = dateFormat.format(orderRecord.getStartTime());
		comsumeDetail.stopTime = dateFormat.format(orderRecord.getStopTime());
		comsumeDetail.totalPauseTime = orderRecord.getTotalPauseTime();
		comsumeDetail.path = orderRecord.getPath();
		comsumeDetail.deviceName = device.getDeviceName();
		comsumeDetail.deviceSn = device.getDeviceSn();
		MybaitsConfig.closeCurrent();
		return JsonResult.data(comsumeDetail);
	}

	@Override
	public JsonResult getRecharge(String token, int id, int tradeType){
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		if (TradeTypeEnum.RECHARGE.getValue() != tradeType) {
			logger.error("非充值类型账单");
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		SqlSession sqlSession = MybaitsConfig.getCurrent();
		BillRecordsMapper billRecordsDao = sqlSession.getMapper(BillRecordsMapper.class);
		BillRecords billRecords = billRecordsDao.getById(id);
		if (billRecords == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		RechargeDetail rechargeDetail = new RechargeDetail();
		rechargeDetail.id = billRecords.getId();
		rechargeDetail.tradeType = billRecords.getTradeType().getValue();
		rechargeDetail.tradeStatusEnum = billRecords.getTradeStatusEnum().getValue();
		rechargeDetail.tradingSn = billRecords.getTradingSn();
		rechargeDetail.tradeTime = dateFormat.format(billRecords.getCreatedDateTime());
		rechargeDetail.totalFee = billRecords.getTotalFee();
		MybaitsConfig.closeCurrent();
		return JsonResult.data(rechargeDetail);
	}
	
	class RecordInfos{
		public int pageIndex;
		public int pageSize;
		public Collection<RecordInfo> records;
	}
	
	class RecordInfo{
		public long id;
		public int tradeType;
		public int tradeStatusEnum;
		public String tradingSn;
		public String tradeTime;
		public int totalFee;
	}
	
	class ComsumeDetail extends RecordInfo{
		public String startTime;
		public int totalPauseTime;
		public String stopTime;
		public String deviceName;
		public String deviceSn;
		public String path;
	}
	
	class RechargeDetail extends RecordInfo{
		
	}
}