package com.young.common.enums;

import lombok.Getter;

/**
 * KYC实名认证状态枚举
 */
@Getter
public enum KycStatus {

    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "驳回");

    private final int code;
    private final String desc;

    KycStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static KycStatus fromCode(int code) {
        for (KycStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的KYC状态代码: " + code);
    }

    /**
     * 判断是否为待审核状态
     */
    public static boolean isPending(Integer status) {
        return status != null && status == PENDING.code;
    }

    /**
     * 判断是否为审核通过状态
     */
    public static boolean isApproved(Integer status) {
        return status != null && status == APPROVED.code;
    }

    /**
     * 判断是否为驳回状态
     */
    public static boolean isRejected(Integer status) {
        return status != null && status == REJECTED.code;
    }
}