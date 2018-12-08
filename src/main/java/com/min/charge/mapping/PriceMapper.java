package com.min.charge.mapping;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.Price;

public interface PriceMapper {

	Price getById(@Param("id") int id);
	
	int save(Price price);
	
	Price getByNow();
	
	int update(Price price);
}
