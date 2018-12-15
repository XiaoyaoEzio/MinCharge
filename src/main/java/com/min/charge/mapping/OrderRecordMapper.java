package com.min.charge.mapping;

import com.min.charge.beans.OrderRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderRecordMapper {

	OrderRecord getById(@Param("id") long id);
	
	OrderRecord getByTradeSn(@Param("tradingSn") String tradingSn);
	
	Collection<OrderRecord> getByDeviceId_Working(@Param("deviceId") int deviceId);
	
	int save(OrderRecord orderRecord);
	
	int update(OrderRecord orderRecord);
	
}
