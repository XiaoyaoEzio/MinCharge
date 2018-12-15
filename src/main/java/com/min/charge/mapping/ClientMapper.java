package com.min.charge.mapping;

import org.apache.ibatis.annotations.Param;

import com.min.charge.beans.Client;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientMapper {

	/**
	 * 根据Id获取用户信息
	 * @param id
	 * @return
	 */
	Client getById(@Param("id") int id);
	
	Client getByName(@Param("userName") String userName);
	
	Client getByOpenId(@Param("publicOpenId") String publicOpenId);
	
	int save(Client client);
	
	int update(Client client);
	
	int updateBalance(@Param("clientId") int clientId,
			@Param("totalFee") int totalFee);
}
