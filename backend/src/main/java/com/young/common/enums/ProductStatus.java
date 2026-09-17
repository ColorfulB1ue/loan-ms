package com.young.common.enums;

import lombok.Getter;

/**
 * 贷款产品状态枚举
 */
@Getter
public enum ProductStatus {

    OFFLINE(0, "下架"),
    ONLINE(1, "上架中");

    private final int code;
    private final String desc;

    ProductStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static ProductStatus fromCode(int code) {
        for (ProductStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的产品状态代码: " + code);
    }

    /**
     * 判断是否为上架状态
     */
    public static boolean isOnline(Integer status) {
        return status != null && status == ONLINE.code;
    }
}