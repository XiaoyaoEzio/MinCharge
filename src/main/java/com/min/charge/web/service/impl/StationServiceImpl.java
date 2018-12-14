package com.min.charge.web.service.impl;

import java.util.Collection;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.min.charge.beans.Station;
import com.min.charge.beans.User;
import com.min.charge.buffer.WebLoginBuffer;
import com.min.charge.config.MybatisConfig;
import com.min.charge.enums.ErrorCodeEnum;
import com.min.charge.json.JsonResult;
import com.min.charge.mapping.StationMapper;
import com.min.charge.web.service.StationService;

@Service
public class StationServiceImpl implements StationService{
	
	private static final Logger logger = Logger.getLogger(StationServiceImpl.class);
	
	@Override
	public JsonResult save(String webToken, String stationName, float latitude,
			float longitude, String provinceName, String provinceCode,
			String cityName, String cityCode, String streetName) {
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			logger.error("webToken: " + webToken);
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybatisConfig.getCurrent();
		StationMapper stationMapper = session.getMapper(StationMapper.class);
		Station station = stationMapper.getByName(stationName);
		if (station!=null) {
			return JsonResult.code(ErrorCodeEnum.STATIONNAME_HAS_EXIST);
		}
		Date now = new Date();
		station = new Station();
		station.setCityCode(cityCode);
		station.setCityName(cityName);
		station.setHasDeleted(false);
		station.setCreatedDateTime(now);
		station.setLatitude(latitude);
		station.setLongitude(longitude);
		station.setProvinceCode(provinceCode);
		station.setProvinceName(provinceName);
		station.setStationName(stationName);
		station.setStreetName(streetName);
		station.setUpdateDateTime(now);
		stationMapper.save(station);
		session.commit(true);
		MybatisConfig.closeCurrent();
		
		return JsonResult.data(station);
	}
	
	@Override
	public JsonResult query(String webToken, int pageIndex, int pageSize, String stationName){
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			logger.error("webToken: " + webToken);
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybatisConfig.getCurrent();
		StationMapper stationMapper = session.getMapper(StationMapper.class);
		
		Collection<Station> stations = stationMapper.getPageSerache(pageSize*(pageIndex-1), pageIndex*pageSize, stationName);
		StationInfo stationInfo = new StationInfo();
		stationInfo.pageIndex = pageIndex;
		stationInfo.pageSize = pageSize;
		stationInfo.stations = stations;
		return JsonResult.data(stationInfo);
	}

	@Override
	public JsonResult update(String webToken, int id,String stationName, float latitude,
			float longitude, String provinceName, String provinceCode,
			String cityName, String cityCode, String streetName){
		User user = WebLoginBuffer.getClient(webToken);
		if (user == null) {
			logger.error("webToken: " + webToken);
			return JsonResult.code(ErrorCodeEnum.TOKEN_INVAILD);
		}
		SqlSession session = MybatisConfig.getCurrent();
		StationMapper stationMapper = session.getMapper(StationMapper.class);
		Station station = stationMapper.getById(id);
		if (station == null) {
			return JsonResult.code(ErrorCodeEnum.DATA_NOT_FOUND);
		}
		Date now = new Date();
		if (cityCode!=null) 
			station.setCityCode(cityCode);
		if (cityName!=null) 
			station.setCityName(cityName);
		if (latitude!=0) 
			station.setLatitude(latitude);
		if (longitude!=0) 
			station.setLongitude(longitude);
		if (provinceCode!=null) 
			station.setProvinceCode(provinceCode);
		if (provinceName!=null) 
			station.setProvinceName(provinceName);
		if (stationName!=null) 
			station.setStationName(stationName);
		if (streetName!=null) 
			station.setStreetName(streetName);
		station.setUpdateDateTime(now);
		stationMapper.save(station);
		session.commit(true);
		MybatisConfig.closeCurrent();
		
		return JsonResult.data(station);
	}
	

	class StationInfo{
		
		public int pageIndex;
		public int pageSize;
		public Collection<Station> stations;
	}
	
	
}
