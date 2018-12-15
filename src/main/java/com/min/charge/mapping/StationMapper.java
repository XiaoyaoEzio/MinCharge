package com.min.charge.mapping;

import com.min.charge.beans.Station;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
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
