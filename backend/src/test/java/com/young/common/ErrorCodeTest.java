package com.young.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 错误码枚举测试
 */
class ErrorCodeTest {

    @Test
    void testSuccessCode() {
        assertEquals(200, ErrorCode.SUCCESS.getCode());
        assertEquals("操作成功", ErrorCode.SUCCESS.getMessage());
    }

    @Test
    void testClientErrorCodes() {
        assertEquals(400, ErrorCode.BAD_REQUEST.getCode());
        assertEquals(401, ErrorCode.UNAUTHORIZED.getCode());
        assertEquals(403, ErrorCode.FORBIDDEN.getCode());
        assertEquals(404, ErrorCode.NOT_FOUND.getCode());
        assertEquals(429, ErrorCode.TOO_MANY_REQUESTS.getCode());
    }

    @Test
    void testServerErrorCodes() {
        assertEquals(500, ErrorCode.INTERNAL_ERROR.getCode());
    }

    @Test
    void testBusinessErrorCodes() {
        // 认证模块
        assertTrue(ErrorCode.LOGIN_FAILED.getCode() > 1000000);
        assertTrue(ErrorCode.ACCOUNT_LOCKED.getCode() > 1000000);
        
        // 用户模块
        assertTrue(ErrorCode.USER_NOT_FOUND.getCode() > 2000000);
        assertTrue(ErrorCode.KYC_NOT_SUBMITTED.getCode() > 2000000);
        
        // 贷款模块
        assertTrue(ErrorCode.LOAN_NOT_FOUND.getCode() > 3000000);
        assertTrue(ErrorCode.PRODUCT_NOT_FOUND.getCode() > 3000000);
        
        // 额度模块
        assertTrue(ErrorCode.CREDIT_INSUFFICIENT.getCode() > 4000000);
        
        // 还款模块
        assertTrue(ErrorCode.PLAN_NOT_FOUND.getCode() > 5000000);
    }

    @Test
    void testFromCode() {
        assertEquals(ErrorCode.SUCCESS, ErrorCode.fromCode(200));
        assertEquals(ErrorCode.BAD_REQUEST, ErrorCode.fromCode(400));
        assertEquals(ErrorCode.UNAUTHORIZED, ErrorCode.fromCode(401));
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.fromCode(404));
        assertEquals(ErrorCode.INTERNAL_ERROR, ErrorCode.fromCode(500));
    }

    @Test
    void testFromCodeWithInvalidCode() {
        // 无效的错误码应返回INTERNAL_ERROR
        assertEquals(ErrorCode.INTERNAL_ERROR, ErrorCode.fromCode(999));
        assertEquals(ErrorCode.INTERNAL_ERROR, ErrorCode.fromCode(0));
        assertEquals(ErrorCode.INTERNAL_ERROR, ErrorCode.fromCode(-1));
    }
}