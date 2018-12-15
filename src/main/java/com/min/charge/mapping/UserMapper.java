package com.min.charge.mapping;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
	
	/** 根据Id获取用户信息
	 * @param id
	 * @return
	 */
	User getById(@Param("id") int id);
	
	User getByName(@Param("userName") String userName);
	
	int save(User client);
}
