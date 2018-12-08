package com.min.charge.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.min.charge.enums.TradeStatusEnum;
import com.min.charge.enums.TradeTypeEnum;

public class BillRecords {

	private Long id;

	private Date createdDateTime;
	
	@JsonProperty
	private Integer tradeType;
	
	private Integer tradeStatusEnum;
	
	private Integer clientId;
	
	private Integer priceId;
	
	private Integer deviceId;
	
	private String tradingSn;
	
	private Integer totalFee;

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

	
	public TradeTypeEnum getTradeType() {
		if (tradeType == null) {
			return null;
		}
		return TradeTypeEnum.valueOf(tradeType);
	}

	public void setTradeType(TradeTypeEnum tradeType) {
		if (tradeType == null) {
			this.tradeType = null;
		}
		this.tradeType = tradeType.getValue();
	}

	public TradeStatusEnum getTradeStatusEnum() {
		if (tradeStatusEnum == null) {
			return null;
		}
		return TradeStatusEnum.valueOf(tradeStatusEnum);
	}

	public void setTradeStatusEnum(TradeStatusEnum tradeStatusEnum) {
		if (tradeStatusEnum == null) {
			this.tradeStatusEnum = null;
		}
		this.tradeStatusEnum = tradeStatusEnum.getValue();
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

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}
	
	
	
}
