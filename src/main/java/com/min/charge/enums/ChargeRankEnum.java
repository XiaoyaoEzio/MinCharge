package com.min.charge.enums;

/**
 * 充电时间级别
 */
public enum ChargeRankEnum {
    TWO(0, 2, "二小时"),
    FOUR(1, 4, "四小时"),
    SIX(2, 6, "六小时"),
    EIGHT(3, 8, "八小时"),
    AUTO(4, 0, "自动充满");

    /**
     * 充电等级
     */
    private int rank;

    /**
     * 充电时长, 0表示自动充满
     */
    private int value;

    /**
     * 名称
     */
    private String name;

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    ChargeRankEnum(int rank, int value, String name) {
        this.rank = rank;
        this.value = value;
        this.name = name;
    }
}
