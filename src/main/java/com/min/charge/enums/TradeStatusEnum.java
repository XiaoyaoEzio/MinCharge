package com.min.charge.enums;

public enum TradeStatusEnum implements BaseEnum{

	Finished(0,"交易完成"),
	Waiting(1,"等待支付");
	
	private Integer value;
	
	private String desc;
	
	private TradeStatusEnum(Integer num, String desc ){
		this.value = num;
		this.desc = desc; 
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static TradeStatusEnum valueOf(Integer value){
		TradeStatusEnum enumBuffer = null;
		for (TradeStatusEnum en : TradeStatusEnum.values()) {
			if (en.getValue() == value ) {
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
