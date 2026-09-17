package com.young.common.enums;

import lombok.Getter;

/**
 * 贷款申请状态枚举
 */
@Getter
public enum LoanStatus {

    PENDING(0, "待审批"),
    APPROVED(1, "审批通过(已放款)"),
    REJECTED(2, "审批驳回"),
    SETTLED(3, "已结清");

    private final int code;
    private final String desc;

    LoanStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static LoanStatus fromCode(int code) {
        for (LoanStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的贷款状态代码: " + code);
    }

    /**
     * 判断是否为待审批状态
     */
    public static boolean isPending(Integer status) {
        return status != null && status == PENDING.code;
    }

    /**
     * 判断是否为已放款状态
     */
    public static boolean isApproved(Integer status) {
        return status != null && status == APPROVED.code;
    }

    /**
     * 判断是否为已结清状态
     */
    public static boolean isSettled(Integer status) {
        return status != null && status == SETTLED.code;
    }
}