package com.min.charge.mapping;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.First;

public interface FirstMapper {
	
	/**
	 * 获取企业号信息
	 * @param id
	 * @return
	 */
	First getId(@Param("id") int id);
}
