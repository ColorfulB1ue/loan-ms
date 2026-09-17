package com.young.common.enums;

import lombok.Getter;

/**
 * 还款计划状态枚举
 */
@Getter
public enum PlanStatus {

    PENDING(0, "待还"),
    PAID(1, "已还清"),
    OVERDUE(2, "逾期中"),
    EARLY_SETTLED(3, "提前结清");

    private final int code;
    private final String desc;

    PlanStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static PlanStatus fromCode(int code) {
        for (PlanStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的还款计划状态代码: " + code);
    }

    /**
     * 判断是否为待还状态
     */
    public static boolean isPending(Integer status) {
        return status != null && status == PENDING.code;
    }

    /**
     * 判断是否为已还清状态
     */
    public static boolean isPaid(Integer status) {
        return status != null && status == PAID.code;
    }

    /**
     * 判断是否为逾期状态
     */
    public static boolean isOverdue(Integer status) {
        return status != null && status == OVERDUE.code;
    }

    /**
     * 判断是否为已结清状态（已还清或提前结清）
     */
    public static boolean isSettled(Integer status) {
        return status != null && (status == PAID.code || status == EARLY_SETTLED.code);
    }

    /**
     * 判断是否为未结清状态（待还或逾期）
     */
    public static boolean isUnsettled(Integer status) {
        return status != null && (status == PENDING.code || status == OVERDUE.code);
    }
}