package com.min.charge.enums;


public enum TradeTypeEnum implements BaseEnum{

	COMSUME(1,"消费"),
	
	RECHARGE(2,"充值");
	
	private Integer value;
	
	private String desc;
	
	private TradeTypeEnum(Integer value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	};
	
	public static TradeTypeEnum valueOf(Integer value){
		TradeTypeEnum enumBuffer=null;
		for(TradeTypeEnum en: TradeTypeEnum.values()){
			if(en.value==value){
				enumBuffer=en;
				break;
			}
		}
		if(enumBuffer==null){
			throw new RuntimeException("TradeTypeEnum Enum value not exist");
		}
		return enumBuffer;
	}
	
	
}
