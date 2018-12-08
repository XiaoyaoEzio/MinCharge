package com.min.charge.beans;

/**
 * 用户表
 * @author 
 *
 */
public class Client {

	private Integer id;
	
	private String userName;
	
	private String password;
	
	private String phoneNum;
	
	private Integer balance;
	
	private String sessionId;
	
	private Integer gender;
	
	private String signature;
	
	private String publicOpenId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPublicOpenId() {
		return publicOpenId;
	}

	public void setPublicOpenId(String publicOpenId) {
		this.publicOpenId = publicOpenId;
	}
	
	
}
