package com.min.charge.mapping;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.Micropay;

public interface MicroPayMapper {

	int save(Micropay micropay);
	
	Micropay getId(@Param("Id") long id);
	
	Micropay getTradingId(@Param("tradingId") long id);
	
	Micropay getTradingSn(@Param("tradingSn") String tradingSn);
	
}
