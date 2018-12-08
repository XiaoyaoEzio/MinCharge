package com.min.charge.mapping;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.Device;
import com.min.charge.beans.Station;

public interface StationMapper {
	
	Station getById(@Param("id") int id);
	
	int save(Station station);
	
	int deleted(Station station);
	
	int updated(Station station);
	
	Station getByName(@Param("stationName") String stationName);
	
	Collection<Station> getPageSerache(@Param("pageIndex") int pageIndex,
			@Param("pageSize") int pageSize,
			@Param("stationName") String stationName);
	

}
