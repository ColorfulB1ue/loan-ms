package com.young.common.enums;

import lombok.Getter;

/**
 * 授信额度状态枚举
 */
@Getter
public enum CreditStatus {

    FROZEN(0, "被冻结"),
    ACTIVE(1, "正常使用");

    private final int code;
    private final String desc;

    CreditStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static CreditStatus fromCode(int code) {
        for (CreditStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的额度状态代码: " + code);
    }

    /**
     * 判断是否为正常状态
     */
    public static boolean isActive(Integer status) {
        return status != null && status == ACTIVE.code;
    }

    /**
     * 判断是否为冻结状态
     */
    public static boolean isFrozen(Integer status) {
        return status != null && status == FROZEN.code;
    }
}