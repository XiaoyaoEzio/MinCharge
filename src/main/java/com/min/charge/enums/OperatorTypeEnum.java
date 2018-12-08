package com.min.charge.enums;

public enum OperatorTypeEnum {

	/**
	 * 启动
	 */
	Start(1,"start"),
	
	/**
	 * 停止
	 */
	Stop(2,"reset"),
	
	/**
	 * 暂停
	 */
	Pause(3,"pause"),
	
	/**
	 * 恢复
	 */
	Regain(4,"regain"),
	
	/**
	 * 状态
	 */
	State(5,"state");
	
	private int operatorType;
	
	private String command;
	
	private OperatorTypeEnum(int operatorType, String command){
		this.operatorType = operatorType;
		this.command = command;
	}

	public int getOperatorType() {
		return operatorType;
	}

	public String getCommand() {
		return command;
	}
	
	public static OperatorTypeEnum valueOf(Integer value){
		OperatorTypeEnum enumBuffer = null;
		for (OperatorTypeEnum en : OperatorTypeEnum.values()) {
			if (en.getOperatorType() == value ) {
				enumBuffer = en;
				break;
			}
		}
		if(enumBuffer==null){
			throw new RuntimeException("TradeStatusEnum Enum value not exist!!!");
		}
		return enumBuffer;
	}
}
