package com.young.common.enums;

import lombok.Getter;

/**
 * 还款类型枚举
 */
@Getter
public enum PayType {

    NORMAL(1, "正常按期还款"),
    OVERDUE(2, "逾期后还款清欠"),
    EARLY_SETTLEMENT(3, "提前全额结清");

    private final int code;
    private final String desc;

    PayType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static PayType fromCode(int code) {
        for (PayType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的还款类型代码: " + code);
    }

    /**
     * 判断是否为正常还款
     */
    public static boolean isNormal(Integer type) {
        return type != null && type == NORMAL.code;
    }

    /**
     * 判断是否为逾期还款
     */
    public static boolean isOverdue(Integer type) {
        return type != null && type == OVERDUE.code;
    }

    /**
     * 判断是否为提前结清
     */
    public static boolean isEarlySettlement(Integer type) {
        return type != null && type == EARLY_SETTLEMENT.code;
    }
}