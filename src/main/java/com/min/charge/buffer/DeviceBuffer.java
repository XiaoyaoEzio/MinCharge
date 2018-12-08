package com.min.charge.buffer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.min.charge.beans.Device;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.mapping.DeviceMapper;

public enum DeviceBuffer {
	Instance;

	private static final Logger logger = Logger.getLogger(DeviceBuffer.class);
	
	private static Map<String, Device>  deviceMap = new HashMap<String, Device>();

	public void init(){
		SqlSession session = MybaitsConfig.getCurrent();
		DeviceMapper deviceDao = session.getMapper(DeviceMapper.class);
		
		Collection<Device> devices = deviceDao.getByAll();
		logger.error("加载设备数量："+ devices.size());
		for (Device device : devices) {
			deviceMap.put(device.getDeviceSn(), device);
		}
		MybaitsConfig.closeCurrent();
	}
	
	public synchronized void add(Device device){
		logger.info("新增设备"+ device.getDeviceSn());
		deviceMap.put(device.getDeviceSn(), device);
	}

	public synchronized void remove(String deviceSn){
		logger.info("移除设备"+ deviceSn);
		deviceMap.remove(deviceSn);
	}
	
	public synchronized Device getByDeviceSn(String deviceSn){
		return deviceMap.get(deviceSn);
	}
	
	public Map<String , Device> getDeviceMap(){
		return deviceMap;
	}
}
