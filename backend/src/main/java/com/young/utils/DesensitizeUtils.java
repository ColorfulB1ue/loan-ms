package com.young.utils;

/**
 * 数据脱敏工具类
 * 用于对敏感信息进行掩码处理，保护用户隐私
 */
public class DesensitizeUtils {

    private DesensitizeUtils() {
        // 工具类，禁止实例化
    }

    /**
     * 身份证号脱敏
     * 保留前4位和后4位，中间用*号替代
     * 示例：3301**********1234
     */
    public static String desensitizeIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "*".repeat(idCard.length() - 8) + idCard.substring(idCard.length() - 4);
    }

    /**
     * 银行卡号脱敏
     * 保留前4位和后4位，中间用*号替代
     * 示例：6222 **** **** 1234
     */
    public static String desensitizeBankCard(String bankCard) {
        if (bankCard == null || bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 手机号脱敏
     * 保留前3位和后4位，中间用*号替代
     * 示例：138****1234
     */
    public static String desensitizePhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 邮箱脱敏
     * 保留@前的前3位和@后的完整域名
     * 示例：zha***@example.com
     */
    public static String desensitizeEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf("@");
        String prefix = email.substring(0, Math.min(3, atIndex));
        String suffix = email.substring(atIndex);
        return prefix + "***" + suffix;
    }

    /**
     * 姓名脱敏
     * 保留姓氏，名字用*号替代
     * 示例：张*、张**
     */
    public static String desensitizeName(String name) {
        if (name == null || name.length() < 2) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }

    /**
     * 通用脱敏方法
     * 保留前N位和后N位，中间用*号替代
     */
    public static String desensitizeMiddle(String text, int prefixLen, int suffixLen) {
        if (text == null || text.length() <= prefixLen + suffixLen) {
            return text;
        }
        return text.substring(0, prefixLen) +
               "*".repeat(text.length() - prefixLen - suffixLen) +
               text.substring(text.length() - suffixLen);
    }
}
