package com.young.common.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRole {

    CUSTOMER(0, "客户"),
    ADMIN(1, "管理员");

    private final int code;
    private final String desc;

    UserRole(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static UserRole fromCode(int code) {
        for (UserRole role : values()) {
            if (role.code == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的用户角色代码: " + code);
    }

    /**
     * 判断是否为管理员
     */
    public static boolean isAdmin(Integer role) {
        return role != null && role == ADMIN.code;
    }

    /**
     * 判断是否为客户
     */
    public static boolean isCustomer(Integer role) {
        return role != null && role == CUSTOMER.code;
    }
}