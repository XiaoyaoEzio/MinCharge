package com.min.charge.beans;

import java.util.Date;

import com.min.charge.enums.OrderStatusEnum;

public class OrderRecord {
	
	private Long id;

	private Date createdDateTime;
	
	private Date startTime;
	
	private Date stopTime;
	
	private Integer clientId;
	
	private Integer priceId;
	
	private Integer deviceId;
	
	private String tradingSn;
	
	private Date lastPauseTime;
	
	private Integer orderStatusEnum;
	
	private Integer totalPauseTime;
	
	private String path;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Integer getPriceId() {
		return priceId;
	}

	public void setPriceId(Integer priceId) {
		this.priceId = priceId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getTradingSn() {
		return tradingSn;
	}

	public void setTradingSn(String tradingSn) {
		this.tradingSn = tradingSn;
	}
	
	public Date getLastPauseTime() {
		return lastPauseTime;
	}

	public void setLastPauseTime(Date lastPauseTime) {
		this.lastPauseTime = lastPauseTime;
	}

	public OrderStatusEnum getOrderStatusEnum() {
		if (orderStatusEnum == null) {
			return null;
		}
		
		return OrderStatusEnum.valueOf(orderStatusEnum);
	}

	public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
		if (orderStatusEnum == null) {
			this.orderStatusEnum = null;
		}
		this.orderStatusEnum = orderStatusEnum.getValue();
	}

	public Integer getTotalPauseTime() {
		return totalPauseTime;
	}

	public void setTotalPauseTime(Integer totalPauseTime) {
		this.totalPauseTime = totalPauseTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
}
