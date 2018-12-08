package com.min.charge.buffer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;


public enum SessionBuffer {

	
	Instance;
	
	private static Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();


	private static Map<String, HttpSession> webSessionMap = new HashMap<String, HttpSession>();

	
	public static synchronized void add(HttpSession session){
		if (session != null) {
			sessionMap.put(session.getId(), session);
		}
	}
	
	public static synchronized HttpSession getSession(String sessionId) {
		if (sessionId == null) 
			return null;
		return sessionMap.get(sessionId);
	}
	
	public static synchronized void remove(HttpSession session){
		if (session != null) {
			sessionMap.remove(session.getId());
		}
	}
	
	public static synchronized void webAdd(HttpSession session){
		if (session != null) {
			webSessionMap.put(session.getId(), session);
		}
	}
	
	public static synchronized HttpSession getWebSession(String sessionId) {
		if (sessionId == null) 
			return null;
		return webSessionMap.get(sessionId);
	}
	
	public static synchronized void removeWebSession(HttpSession session){
		if (session != null) {
			webSessionMap.remove(session.getId());
		}
	}
}
