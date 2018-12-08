package com.min.charge.beans;

import java.util.Date;

public class Device {

	private Integer id;
	
	private Date createdDateTime;
	
	private String deviceSn;
	
	private Date lastOperatorTime;
	
	private Integer deviceStatus;
	
	private Integer stationId;
	
	private Boolean hasDeleted;
	
	private String deviceName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public Date getLastOperatorTime() {
		return lastOperatorTime;
	}

	public void setLastOperatorTime(Date lastOperatorTime) {
		this.lastOperatorTime = lastOperatorTime;
	}

	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public Boolean getHasDeleted() {
		return hasDeleted;
	}

	public void setHasDeleted(Boolean hasDeleted) {
		this.hasDeleted = hasDeleted;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	
}
