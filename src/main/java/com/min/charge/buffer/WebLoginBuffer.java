package com.min.charge.buffer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;

import com.min.charge.beans.User;
import com.min.charge.config.MybaitsConfig;
import com.min.charge.mapping.UserMapper;

public enum WebLoginBuffer {

	Instance;
	
	public static final String WEB_TOKEN_KEY = "User_Id";
	
	private static Map<Integer, HttpSession> userMap = new HashMap<Integer, HttpSession>();

	public static synchronized void bindUser(HttpSession session,Integer userId){
		if (session == null) {
			return;
		}
		userMap.remove(userId);
		session.setAttribute(WEB_TOKEN_KEY, userId);
		userMap.put(userId, session);
	}
	
	public static synchronized User getClient(String sessionId) {
		HttpSession session = SessionBuffer.getSession(sessionId);
		if (session == null) {
			return null;
		}
		Integer clientId = (Integer)session.getAttribute(WEB_TOKEN_KEY);
		if (clientId == null) {
			return null;
		}
		SqlSession sqlSession  = MybaitsConfig.getCurrent();
		UserMapper clientDao = sqlSession.getMapper(UserMapper.class);
		User client = clientDao.getById(clientId);
		MybaitsConfig.closeCurrent();
		return  client;
	}
	

	
	public static synchronized void remove(Integer	userId){
		userMap.remove(userId);
	}
}
