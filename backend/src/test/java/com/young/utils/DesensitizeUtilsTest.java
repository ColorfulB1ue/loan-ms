package com.young.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 脱敏工具类测试
 */
class DesensitizeUtilsTest {

    @Test
    void testDesensitizeIdCard() {
        assertEquals("1101**********888X", DesensitizeUtils.desensitizeIdCard("11010519990101888X"));
        assertEquals("1101****5678", DesensitizeUtils.desensitizeIdCard("110105199901015678"));
        assertNull(DesensitizeUtils.desensitizeIdCard(null));
        assertEquals("1234567", DesensitizeUtils.desensitizeIdCard("1234567")); // 长度不足
    }

    @Test
    void testDesensitizeBankCard() {
        assertEquals("6227 **** **** 5678", DesensitizeUtils.desensitizeBankCard("6227000012345678"));
        assertNull(DesensitizeUtils.desensitizeBankCard(null));
        assertEquals("1234567", DesensitizeUtils.desensitizeBankCard("1234567")); // 长度不足
    }

    @Test
    void testDesensitizePhone() {
        assertEquals("138****8000", DesensitizeUtils.desensitizePhone("13800138000"));
        assertNull(DesensitizeUtils.desensitizePhone(null));
        assertEquals("123456", DesensitizeUtils.desensitizePhone("123456")); // 长度不足
    }

    @Test
    void testDesensitizeEmail() {
        assertEquals("zha***@example.com", DesensitizeUtils.desensitizeEmail("zhangsan@example.com"));
        assertEquals("li***@test.com", DesensitizeUtils.desensitizeEmail("li@test.com"));
        assertNull(DesensitizeUtils.desensitizeEmail(null));
        assertEquals("invalid", DesensitizeUtils.desensitizeEmail("invalid")); // 无@
    }

    @Test
    void testDesensitizeName() {
        assertEquals("张*", DesensitizeUtils.desensitizeName("张三"));
        assertEquals("张**", DesensitizeUtils.desensitizeName("张三丰"));
        assertEquals("张***", DesensitizeUtils.desensitizeName("张三丰四"));
        assertNull(DesensitizeUtils.desensitizeName(null));
        assertEquals("张", DesensitizeUtils.desensitizeName("张")); // 长度不足
    }

    @Test
    void testDesensitizeMiddle() {
        assertEquals("12****78", DesensitizeUtils.desensitizeMiddle("12345678", 2, 2));
        assertEquals("1****7890", DesensitizeUtils.desensitizeMiddle("1234567890", 1, 4));
        assertNull(DesensitizeUtils.desensitizeMiddle(null, 2, 2));
        assertEquals("1234", DesensitizeUtils.desensitizeMiddle("1234", 2, 2)); // 长度不足
    }
}