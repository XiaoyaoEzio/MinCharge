package com.min.charge.sevice.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min.charge.beans.Client;
import com.min.charge.beans.Device;
import com.min.charge.beans.OrderRecord;
import com.min.charge.beans.Price;
import com.min.charge.buffer.ChargeInfoBuffer;
import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.buffer.LoginBuffer;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.enums.OrderStatusEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.ClientMapper;
import com.min.charge.mapping.DeviceMapper;
import com.min.charge.mapping.PriceMapper;
import com.min.charge.sevice.ChargingInfoService;

@Service
public class ChargingInfoServiceImpl implements ChargingInfoService{

	@Autowired
	private ClientMapper clientMapper;
	
	@Autowired
	private PriceMapper priceMapper;
	
	@Autowired
	private DeviceMapper deviceMapper;
	
	@Override
	public JsonResult refresh(String token, String deviceSn) {
		Client client = LoginBuffer.getClient(token);
		if (client == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		OrderRecord record = ChargeInfoBuffer.Instance.getByClientId(client.getId());
		if (record == null) {
			return JsonResult.code(ErrorCodeEnum.NO_CHARGING);
		}
		if(deviceSn == null)
			deviceSn = deviceMapper.getById(record.getDeviceId()).getDeviceSn();
		Device device = DeviceBuffer.Instance.getByDeviceSn(deviceSn);
		Price price = priceMapper.getById(record.getPriceId());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.beginTime = dateFormat.format(record.getStartTime());
		chargeInfo.chargeTime = (int)((now.getTime() - record.getStartTime().getTime())/(60*1000))-record.getTotalPauseTime()/(60*1000); 
		chargeInfo.deviceName = device.getDeviceName();
		chargeInfo.deviceSn = device.getDeviceSn();
		chargeInfo.price = price.getCommonPrice();
		chargeInfo.totalPauseTime = record.getTotalPauseTime()/(60*1000);
		
		if (record.getOrderStatusEnum().compareTo(OrderStatusEnum.Pause) == 0) {
			chargeInfo.chargeTime = (int)((record.getLastPauseTime().getTime() - record.getStartTime().getTime())/(60*1000))-record.getTotalPauseTime()/(60*1000); 
			chargeInfo.totalPauseTime = (int)((now.getTime() - record.getLastPauseTime().getTime())/(60*1000))+record.getTotalPauseTime()/(60*1000);
		}
		chargeInfo.totalFee = price.getCommonPrice() * chargeInfo.chargeTime;
		chargeInfo.orderStateEnum = record.getOrderStatusEnum().getValue();
		chargeInfo.path = record.getPath();
		return JsonResult.data(chargeInfo);
	}

	class ChargeInfo{
		
		public String deviceSn;
		
		public String deviceName;
		
		public int totalFee;
		
		public int price ;
		
		public String beginTime;
		
		public int chargeTime;
		
		public int  totalPauseTime;
		
		public int orderStateEnum;
		
		public String path;
	
	}
}
