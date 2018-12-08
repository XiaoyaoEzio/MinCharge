package com.min.charge.buffer;

import java.util.concurrent.ConcurrentHashMap;

public enum TradingSnBuffer{
	Instance;
	
	private ConcurrentHashMap<String, String> map =new ConcurrentHashMap<String, String>();
	
	public void put(String tradeSn){
		
		map.put(tradeSn, tradeSn);
		
	}
	
	public boolean get(String tradeSn) {
		return map.containsKey(tradeSn);
	}
	
	public void remove(String tradeSn){
		map.remove(tradeSn);
	}
}
