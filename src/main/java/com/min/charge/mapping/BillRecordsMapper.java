package com.min.charge.mapping;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.BillRecords;

public interface BillRecordsMapper {

	BillRecords getById(@Param("id") int id);
	
	int save(BillRecords billRecords);
	
	int update(BillRecords billRecords);
	
	BillRecords getBySn(@Param("tradingSn") String tradingSn);
	
	List<BillRecords> getAll(@Param("clientId") int clientId,
			@Param("pageIndex") int pageIndex,
			@Param("pageSize") int pageSize);
}
