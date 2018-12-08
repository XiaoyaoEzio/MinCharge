package com.min.charge.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.SysParam;

public interface SysParamMapper {

	List<SysParam> getAll();
	
	SysParam getById(@Param("id") int id);
	
	SysParam getBykey(@Param("paramKey") String paramKey);
	
	int save(SysParam sysParam);
	
	int update(SysParam sysParam);
}
