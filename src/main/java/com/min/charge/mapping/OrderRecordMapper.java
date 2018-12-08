package com.min.charge.mapping;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.OrderRecord;

public interface OrderRecordMapper {

	OrderRecord getById(@Param("id") long id);
	
	OrderRecord getByTradeSn(@Param("tradingSn") String tradingSn);
	
	Collection<OrderRecord> getByDeviceId_Working(@Param("deviceId") int deviceId);
	
	int save(OrderRecord orderRecord);
	
	int update(OrderRecord orderRecord);
	
}
