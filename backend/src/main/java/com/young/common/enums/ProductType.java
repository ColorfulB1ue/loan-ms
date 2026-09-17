package com.young.common.enums;

import lombok.Getter;

/**
 * 贷款产品类型枚举
 */
@Getter
public enum ProductType {

    CONSUMER(0, "消费贷"),
    BUSINESS(1, "经营贷"),
    HOUSE(2, "房贷"),
    CAR(3, "车贷");

    private final int code;
    private final String desc;

    ProductType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static ProductType fromCode(int code) {
        for (ProductType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的产品类型代码: " + code);
    }
}