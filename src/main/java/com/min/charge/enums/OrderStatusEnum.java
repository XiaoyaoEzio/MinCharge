package com.min.charge.enums;

public enum OrderStatusEnum implements BaseEnum{
	
	Start(1),
	Stop(2),
	Pause(3);
	
	
	private Integer value;
	
	private OrderStatusEnum(Integer value){
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	
	
	public static OrderStatusEnum valueOf(int value){
		OrderStatusEnum result = null;
		for (OrderStatusEnum item : OrderStatusEnum.values()) {
			if (item.getValue() == value) {
				result = item;
			}
		}
		if (result == null) {
			throw new RuntimeException("TradeStatusEnum Enum value not exist!!!");
		}
		return result;
	}
}
