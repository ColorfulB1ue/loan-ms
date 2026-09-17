package com.young.common;

import lombok.Getter;

/**
 * 统一错误码枚举
 * 格式：AABBCCC
 * AA - 模块编号
 * BB - 错误类型（00-成功, 01-参数错误, 02-业务错误, 03-权限错误, 04-系统错误）
 * CCC - 具体错误编号
 */
@Getter
public enum ErrorCode {

    // 成功
    SUCCESS(200, "操作成功"),

    // 通用错误 10xxxxx
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "数据冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    // 认证模块错误 10xxxxx
    LOGIN_FAILED(1001001, "用户名或密码错误"),
    ACCOUNT_LOCKED(1001002, "账号已被锁定"),
    TOKEN_EXPIRED(1001003, "Token已过期"),
    TOKEN_INVALID(1001004, "Token无效"),
    PASSWORD_WEAK(1001005, "密码强度不足"),
    PASSWORD_MISMATCH(1001006, "原密码错误"),
    USERNAME_EXISTS(1001007, "用户名已存在"),

    // 用户模块错误 20xxxxx
    USER_NOT_FOUND(2001001, "用户不存在"),
    USER_DISABLED(2001002, "用户已被禁用"),
    KYC_NOT_SUBMITTED(2002001, "未提交实名认证"),
    KYC_PENDING(2002002, "实名认证审核中"),
    KYC_REJECTED(2002003, "实名认证未通过"),
    KYC_ALREADY_SUBMITTED(2002004, "已提交实名材料，请勿重复提交"),

    // 贷款模块错误 30xxxxx
    LOAN_NOT_FOUND(3001001, "贷款申请不存在"),
    LOAN_STATUS_ERROR(3001002, "贷款状态不允许此操作"),
    LOAN_AMOUNT_INVALID(3001003, "贷款金额不合法"),
    LOAN_TERM_INVALID(3001004, "贷款期限不合法"),
    PRODUCT_NOT_FOUND(3002001, "贷款产品不存在"),
    PRODUCT_OFFLINE(3002002, "贷款产品已下架"),

    // 额度模块错误 40xxxxx
    CREDIT_INSUFFICIENT(4001001, "可用额度不足"),
    CREDIT_FROZEN(4001002, "额度已被冻结"),
    CREDIT_APP_PENDING(4002001, "已有待审核的额度申请"),

    // 还款模块错误 50xxxxx
    PLAN_NOT_FOUND(5001001, "还款计划不存在"),
    PLAN_SETTLED(5001002, "账单已结清"),
    PLAN_STATUS_ERROR(5001003, "账单状态不允许此操作"),
    PAY_AMOUNT_INVALID(5001004, "支付金额不合法"),
    PAY_AMOUNT_INSUFFICIENT(5001005, "支付金额不足"),

    // 解冻模块错误 60xxxxx
    UNFREEZE_APP_PENDING(6001001, "已有待审核的解冻申请");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取枚举
     */
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return INTERNAL_ERROR;
    }
}