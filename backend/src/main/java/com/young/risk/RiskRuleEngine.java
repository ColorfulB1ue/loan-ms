package com.young.risk;

import com.young.pojo.LoanApplication;
import com.young.pojo.UserProfile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 风控规则引擎
 * 基于规则的自动审批决策
 */
@Component
public class RiskRuleEngine {

    /**
     * 执行风控规则检查
     */
    public RiskResult evaluate(UserProfile profile, LoanApplication application, 
                               int creditScore, int existingLoanCount) {
        List<String> reasons = new ArrayList<>();
        RiskLevel level = RiskLevel.LOW;

        // 规则1：KYC状态检查
        if (profile == null || profile.getStatus() != 1) {
            reasons.add("实名认证未通过");
            return new RiskResult(RiskLevel.REJECT, reasons);
        }

        // 规则2：信用评分检查
        if (creditScore < 500) {
            reasons.add("信用评分过低: " + creditScore);
            level = RiskLevel.HIGH;
        } else if (creditScore < 600) {
            reasons.add("信用评分偏低: " + creditScore);
            level = RiskLevel.MEDIUM;
        }

        // 规则3：贷款金额检查
        BigDecimal amount = application.getAmount();
        if (amount != null) {
            if (amount.compareTo(new BigDecimal("500000")) > 0) {
                reasons.add("贷款金额超过50万，需要人工审核");
                level = RiskLevel.HIGH;
            } else if (amount.compareTo(new BigDecimal("200000")) > 0) {
                reasons.add("贷款金额超过20万");
                if (level == RiskLevel.LOW) level = RiskLevel.MEDIUM;
            }
        }

        // 规则4：贷款期限检查
        Integer term = application.getTermMonths();
        if (term != null && term > 36) {
            reasons.add("贷款期限超过36个月");
            if (level == RiskLevel.LOW) level = RiskLevel.MEDIUM;
        }

        // 规则5：多头借贷检查
        if (existingLoanCount >= 3) {
            reasons.add("存在多笔未结清贷款: " + existingLoanCount + "笔");
            level = RiskLevel.HIGH;
        } else if (existingLoanCount >= 2) {
            reasons.add("存在多笔未结清贷款: " + existingLoanCount + "笔");
            if (level == RiskLevel.LOW) level = RiskLevel.MEDIUM;
        }

        // 规则6：年龄检查（基于身份证）
        if (profile.getIdCard() != null && profile.getIdCard().length() >= 18) {
            try {
                String birthYear = profile.getIdCard().substring(6, 10);
                int age = java.time.LocalDate.now().getYear() - Integer.parseInt(birthYear);
                if (age < 22 || age > 55) {
                    reasons.add("年龄不在最佳区间: " + age + "岁");
                    if (level == RiskLevel.LOW) level = RiskLevel.MEDIUM;
                }
            } catch (Exception e) {
                // 忽略解析错误
            }
        }

        return new RiskResult(level, reasons);
    }

    /**
     * 风险等级
     */
    public enum RiskLevel {
        LOW,      // 低风险 - 自动通过
        MEDIUM,   // 中风险 - 人工审核
        HIGH,     // 高风险 - 建议拒绝
        REJECT    // 拒绝
    }

    /**
     * 风控结果
     */
    public static class RiskResult {
        private final RiskLevel level;
        private final List<String> reasons;

        public RiskResult(RiskLevel level, List<String> reasons) {
            this.level = level;
            this.reasons = reasons;
        }

        public RiskLevel getLevel() {
            return level;
        }

        public List<String> getReasons() {
            return reasons;
        }

        public boolean canAutoApprove() {
            return level == RiskLevel.LOW;
        }

        public boolean needManualReview() {
            return level == RiskLevel.MEDIUM || level == RiskLevel.HIGH;
        }

        public boolean shouldReject() {
            return level == RiskLevel.REJECT;
        }
    }
}