package com.young.common.enums;

import lombok.Getter;

/**
 * 系统消息状态枚举
 */
@Getter
public enum MessageStatus {

    UNREAD(0, "未读"),
    READ(1, "已读");

    private final int code;
    private final String desc;

    MessageStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static MessageStatus fromCode(int code) {
        for (MessageStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的消息状态代码: " + code);
    }

    /**
     * 判断是否为未读状态
     */
    public static boolean isUnread(Integer status) {
        return status != null && status == UNREAD.code;
    }
}