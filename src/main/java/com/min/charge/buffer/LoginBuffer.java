package com.min.charge.buffer;

import com.min.charge.beans.Client;
import com.min.charge.config.MybatisConfig;
import com.min.charge.mapping.ClientMapper;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public enum LoginBuffer {

	Instance;
	
	public static final String TOKEN_KEY = "Client_Id";
	
	private static Map<Integer, HttpSession> clientMap = new HashMap<>();

	public static synchronized void bindUser(HttpSession session,Integer clientId){
		if (session == null) {
			return;
		}
		clientMap.remove(clientId);
		session.setAttribute(TOKEN_KEY, clientId);
		clientMap.put(clientId, session);
	}
	
	public static synchronized Client getClient(String sessionId) {
		HttpSession session = SessionBuffer.getSession(sessionId);
		if (session == null) {
			return null;
		}
		Integer clientId = (Integer)session.getAttribute(TOKEN_KEY);
		if (clientId == null) {
			return null;
		}
		SqlSession sqlSession  = MybatisConfig.getCurrent();
		ClientMapper clientDao = sqlSession.getMapper(ClientMapper.class);
		Client client = clientDao.getById(clientId);
		MybatisConfig.closeCurrent();
		return  client;
	}
	

	
	public static synchronized void remove(Integer	clientId){
		clientMap.remove(clientId);
	}
	
}
