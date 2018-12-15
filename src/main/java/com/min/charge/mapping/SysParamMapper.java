package com.min.charge.mapping;

import com.min.charge.beans.SysParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysParamMapper {

	List<SysParam> getAll();
	
	SysParam getById(@Param("id") int id);
	
	SysParam getBykey(@Param("paramKey") String paramKey);
	
	int save(SysParam sysParam);
	
	int update(SysParam sysParam);
}
