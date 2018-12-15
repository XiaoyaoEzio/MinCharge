package com.min.charge.mapping;

import com.min.charge.beans.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DeviceMapper {

	Device getById(@Param("id") int id);
	
	Device getByDeviceSn(@Param("deviceSn") String deviceSn);
	
	int save(Device device);
	
	int updateStatus(Device device);
	
	int deleted(Device device);
	
	/**
	 * 获取所有未删除的
	 * @return
	 */
	Collection<Device> getByAll();
	
	/**
	 * 
	 * @param pageIndex pageIndex-1  
	 * @param pageSize
	 * @return
	 */
	Collection<Device> getPageSerache(@Param("pageIndex") int pageIndex,
			@Param("pageSize") int pageSize,
			@Param("stationId") int  stationId);
}
