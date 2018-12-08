package com.min.charge.web.service.impl;

import java.util.Collection;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min.charge.beans.Device;
import com.min.charge.beans.User;
import com.min.charge.buffer.DeviceBuffer;
import com.min.charge.buffer.WebLoginBuffer;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.DeviceMapper;
import com.min.charge.web.service.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService{

	
	
	private static final Logger logger = Logger.getLogger(DeviceServiceImpl.class);
	
	@Override
	public JsonResult save(String webToken, String deviceSn, String deviceName, int stationId) {
		
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			logger.error("webToken: " + webToken);
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybaitsConfig.getCurrent();
		DeviceMapper deviceDao = session.getMapper(DeviceMapper.class);
		Device device = deviceDao.getByDeviceSn(deviceSn);
		if (device!=null) {
			return JsonResult.code(ErrorCodeEnum.DEVICESN_HAS_EXIST);
		}
		Date time = new Date();
		device = new Device();
		device.setCreatedDateTime(time);
		device.setDeviceSn(deviceSn);
		device.setDeviceStatus(-1);
		device.setLastOperatorTime(time);
		device.setDeviceName(deviceName);
		device.setStationId(stationId);
		device.setHasDeleted(false);
		DeviceBuffer.Instance.add(device);
		deviceDao.save(device);
		session.commit(true);
		MybaitsConfig.closeCurrent();
		return JsonResult.data(device);
	}
	
	@Override
	public JsonResult query(String webToken, int pageIndex, int pageSize, int stationId) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybaitsConfig.getCurrent();
		DeviceMapper deviceDao = session.getMapper(DeviceMapper.class);
		Collection<Device> devices = deviceDao.getPageSerache((pageIndex-1)*pageSize, pageSize*pageIndex, stationId);
		DeviceInfo infos = new DeviceInfo();
		infos.pageIndex = pageIndex;
		infos.pageSize = pageSize;
		infos.devices = devices;
		MybaitsConfig.closeCurrent();
		return JsonResult.data(infos);
	}

	@Override
	public JsonResult deleted(String webToken, int id) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybaitsConfig.getCurrent();
		DeviceMapper deviceDao = session.getMapper(DeviceMapper.class);
		Device device = deviceDao.getById(id);
		if (device == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		DeviceBuffer.Instance.remove(device.getDeviceSn());
		deviceDao.deleted(device);
		session.commit(true);
		MybaitsConfig.closeCurrent();
		return new JsonResult();
	}

	@Override
	public JsonResult update(String webToken, int id, String deviceName) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybaitsConfig.getCurrent();
		DeviceMapper deviceDao = session.getMapper(DeviceMapper.class);
		Device device = deviceDao.getById(id);
		if (device == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		device.setDeviceName(deviceName);
		deviceDao.updateStatus(device);
		session.commit(true);
		MybaitsConfig.closeCurrent();
		return JsonResult.data(device);
	}
	
	class DeviceInfo{
		public int pageIndex;
		public int pageSize;
		public Collection<Device> devices;
	}
}
