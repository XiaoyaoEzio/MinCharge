package com.min.charge.buffer;

import com.min.charge.beans.OrderRecord;

import java.util.concurrent.ConcurrentHashMap;

public enum ChargeInfoBuffer {

	Instance;
	
	/**
	 * Integer : ClientId
	 * OrderRecord: 订单记录
	 */
	private static final ConcurrentHashMap<Integer, OrderRecord> clientId_OrderRecord_Map  = new ConcurrentHashMap<Integer, OrderRecord>();
	

	private static final ConcurrentHashMap<String, Integer> tradeSn_ClientId_Map  = new ConcurrentHashMap<String, Integer>();
	
	private static final ConcurrentHashMap<String, String> deviceId_tradeSn_Map = new ConcurrentHashMap<String, String>();

	private static final ConcurrentHashMap<Integer, String> stop_time_job_Map = new ConcurrentHashMap<>();

	private static final ConcurrentHashMap<Integer, String> status_check_job_Map = new ConcurrentHashMap<>();

	public synchronized boolean addRecord(int clientId, OrderRecord record){

		clientId_OrderRecord_Map.put(clientId, record);
		System.err.println(""+record.getTradingSn());
		tradeSn_ClientId_Map.put(record.getTradingSn(), clientId);
		deviceId_tradeSn_Map.put(record.getDeviceId()+"_"+record.getPath(), record.getTradingSn());
		return true;
	}
	
	public OrderRecord getByTradeSn(String tradeSn){
		OrderRecord orderRecord = null;
		Integer clientId = tradeSn_ClientId_Map.get(tradeSn);
		if (clientId == null) 
			return orderRecord;
		orderRecord = clientId_OrderRecord_Map.get(clientId);
		return orderRecord;
	}
	
	public OrderRecord getByClientId(int clientId){
		return clientId_OrderRecord_Map.get(clientId);
	}
	
	public OrderRecord getByDeviceId_Path(int deviceId, String path){
		OrderRecord orderRecord = null;
		String tradeSn = deviceId_tradeSn_Map.get(deviceId+"_"+path);
		if (tradeSn == null) 
			return orderRecord;
		Integer clientId = tradeSn_ClientId_Map.get(tradeSn);
		if (clientId == null) 
			return orderRecord;
		orderRecord = clientId_OrderRecord_Map.get(clientId);
		return orderRecord;
	}
	
	public synchronized boolean removeByTradeSn(String tradeSn){
		System.err.println("移除交易："+tradeSn);
		if (tradeSn_ClientId_Map.containsKey(tradeSn)) {
			int clientId = tradeSn_ClientId_Map.get(tradeSn);
			System.err.println("移除用户："+clientId);
			if (clientId_OrderRecord_Map.containsKey(clientId)) {
				clientId_OrderRecord_Map.remove(clientId);
				tradeSn_ClientId_Map.remove(tradeSn);
			}
			return true;
		}		
		return false;
	}
	
	public synchronized boolean removeByClientId(int clientId){
		if (clientId_OrderRecord_Map.containsKey(clientId)) {
			clientId_OrderRecord_Map.remove(clientId);
			return true;
		}
		return false;
	}
	
	public synchronized boolean removeByDeviceId_Path(int deviceId, String path){
		if (deviceId_tradeSn_Map.containsKey(deviceId+"_"+path)) {
			deviceId_tradeSn_Map.remove(deviceId+"_"+path);
			return true;
		}
		return false;
	}

	public synchronized boolean addSetStopTimeJob(int clientId, String baseJobName) {
		stop_time_job_Map.put(clientId, baseJobName);
		return true;
	}

	public synchronized String removeSetStopTimeJob(int clientId) {
		if (stop_time_job_Map.containsKey(clientId)) {
			return  stop_time_job_Map.remove(clientId);
		}
		return null;
	}

	public synchronized void addStatusCheckJob(int clientId, String baseJobName) {
		status_check_job_Map.put(clientId, baseJobName);
	}

	public synchronized String removeStatusCheckJob(int clientId) {
		if (status_check_job_Map.containsKey(clientId)) {
			return status_check_job_Map.remove(clientId);
		}
		return null;
	}
}
