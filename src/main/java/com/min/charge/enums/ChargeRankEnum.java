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
    private int time;

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

    public int getTime() {
        return time;
    }

    ChargeRankEnum(int rank, int time, String name) {
        this.rank = rank;
        this.time = time;
        this.name = name;
    }

    /**
     * 根据充电级别获取充电时间
     * @param rank 充电级别
     * @return 充电时间，-1表示级别错误
     */
    public static int getTimeByRank(int rank) {
        int[] times = {2, 4, 6, 8, 0};
        if (rank >= 0 && rank <= 4) {
            return times[rank];
        } else {
            return -1;
        }
    }
}
