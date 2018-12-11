package com.min.charge.enums;

/**
 * 充电时间级别
 */
public enum ChargeRankEnum {
    FOUR(1, "四小时"),
    SIX(2, "六小时"),
    EIGHT(3, "八小时"),
    AUTO(4, "自动充满");
    private int value;
    private String name;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ChargeRankEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }
}
