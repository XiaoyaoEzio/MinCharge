package com.min.charge.mapping;


import com.min.charge.beans.BillRecords;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRecordsMapper {

	BillRecords getById(@Param("id") int id);
	
	int save(BillRecords billRecords);
	
	int update(BillRecords billRecords);
	
	BillRecords getBySn(@Param("tradingSn") String tradingSn);
	
	List<BillRecords> getAll(@Param("clientId") int clientId,
			@Param("pageIndex") int pageIndex,
			@Param("pageSize") int pageSize);
}
