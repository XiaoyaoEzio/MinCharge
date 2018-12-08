package com.min.charge.beans;

import java.util.Date;

public class Price {

	private Integer id;
	
	private Integer CommonPrice;
	
	private Date createdDateTime;
	
	private Boolean hasInvaild;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCommonPrice() {
		return CommonPrice;
	}

	public void setCommonPrice(Integer commonPrice) {
		CommonPrice = commonPrice;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public Boolean getHasInvaild() {
		return hasInvaild;
	}

	public void setHasInvaild(Boolean hasInvaild) {
		this.hasInvaild = hasInvaild;
	}
	
	
	
}
