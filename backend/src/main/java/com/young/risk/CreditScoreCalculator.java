package com.young.risk;

import com.young.pojo.LoanApplication;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.UserProfile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 信用评分计算器
 * 基于用户多维度数据计算信用评分
 */
@Component
public class CreditScoreCalculator {

    // 评分权重
    private static final double KYC_WEIGHT = 0.20;        // 实名认证权重
    private static final double REPAYMENT_WEIGHT = 0.35;  // 还款行为权重
    private static final double CREDIT_RATIO_WEIGHT = 0.25; // 负债率权重
    private static final double HISTORY_WEIGHT = 0.20;    // 历史记录权重

    // 基础分
    private static final int BASE_SCORE = 600;
    private static final int MAX_SCORE = 950;
    private static final int MIN_SCORE = 300;

    /**
     * 计算用户信用评分
     */
    public int calculate(UserProfile profile, List<LoanApplication> loans, 
                         List<RepaymentPlan> plans, BigDecimal totalCredit, 
                         BigDecimal usedCredit) {
        double score = BASE_SCORE;

        // 1. KYC完整度评分 (0-100)
        double kycScore = calculateKycScore(profile);
        
        // 2. 还款行为评分 (0-100)
        double repaymentScore = calculateRepaymentScore(plans);
        
        // 3. 负债率评分 (0-100)
        double creditRatioScore = calculateCreditRatioScore(totalCredit, usedCredit);
        
        // 4. 历史记录评分 (0-100)
        double historyScore = calculateHistoryScore(loans);

        // 加权计算
        score += (kycScore * KYC_WEIGHT + 
                  repaymentScore * REPAYMENT_WEIGHT + 
                  creditRatioScore * CREDIT_RATIO_WEIGHT + 
                  historyScore * HISTORY_WEIGHT) * 3.5;

        // 确保在有效范围内
        return (int) Math.max(MIN_SCORE, Math.min(MAX_SCORE, score));
    }

    /**
     * KYC完整度评分
     */
    private double calculateKycScore(UserProfile profile) {
        if (profile == null) return 0;
        
        double score = 0;
        
        // 基本信息完整度
        if (profile.getRealName() != null && !profile.getRealName().isEmpty()) score += 20;
        if (profile.getIdCard() != null && !profile.getIdCard().isEmpty()) score += 20;
        if (profile.getPhone() != null && !profile.getPhone().isEmpty()) score += 15;
        if (profile.getEmail() != null && !profile.getEmail().isEmpty()) score += 10;
        
        // 银行卡信息
        if (profile.getBankCard() != null && !profile.getBankCard().isEmpty()) score += 15;
        if (profile.getBankName() != null && !profile.getBankName().isEmpty()) score += 10;
        
        // 证件照片
        if (profile.getIdCardFront() != null && !profile.getIdCardFront().isEmpty()) score += 5;
        if (profile.getIdCardBack() != null && !profile.getIdCardBack().isEmpty()) score += 5;
        
        return Math.min(100, score);
    }

    /**
     * 还款行为评分
     */
    private double calculateRepaymentScore(List<RepaymentPlan> plans) {
        if (plans == null || plans.isEmpty()) return 50; // 无记录给中等分
        
        int total = plans.size();
        int paidOnTime = 0;
        int overdue = 0;
        int earlySettled = 0;
        
        for (RepaymentPlan plan : plans) {
            if (plan.getStatus() == 1) { // 正常还清
                paidOnTime++;
            } else if (plan.getStatus() == 2) { // 逾期
                overdue++;
            } else if (plan.getStatus() == 3) { // 提前结清
                earlySettled++;
                paidOnTime++; // 提前结清也算按时还款
            }
        }
        
        // 按时还款率
        double onTimeRate = (double) paidOnTime / total;
        
        // 逾期惩罚
        double overdueRate = (double) overdue / total;
        
        // 计算评分
        double score = onTimeRate * 80 - overdueRate * 50;
        
        // 提前结清奖励
        if (earlySettled > 0) {
            score += 20;
        }
        
        return Math.max(0, Math.min(100, score));
    }

    /**
     * 负债率评分
     */
    private double calculateCreditRatioScore(BigDecimal totalCredit, BigDecimal usedCredit) {
        if (totalCredit == null || totalCredit.compareTo(BigDecimal.ZERO) == 0) {
            return 50;
        }
        
        // 计算负债率
        BigDecimal ratio = usedCredit.multiply(new BigDecimal("100"))
                .divide(totalCredit, 2, RoundingMode.HALF_UP);
        
        double ratioValue = ratio.doubleValue();
        
        // 负债率越低越好
        if (ratioValue <= 30) return 100;
        if (ratioValue <= 50) return 80;
        if (ratioValue <= 70) return 60;
        if (ratioValue <= 90) return 40;
        return 20;
    }

    /**
     * 历史记录评分
     */
    private double calculateHistoryScore(List<LoanApplication> loans) {
        if (loans == null || loans.isEmpty()) return 50;
        
        int total = loans.size();
        int settled = 0;
        int rejected = 0;
        
        for (LoanApplication loan : loans) {
            if (loan.getStatus() == 3) { // 已结清
                settled++;
            } else if (loan.getStatus() == 2) { // 被拒绝
                rejected++;
            }
        }
        
        // 成功结清率
        double settleRate = total > 0 ? (double) settled / total : 0;
        
        // 拒绝率惩罚
        double rejectRate = total > 0 ? (double) rejected / total : 0;
        
        // 计算评分
        double score = 60 + settleRate * 40 - rejectRate * 30;
        
        // 历史记录越多且良好，分数越高
        if (settled >= 3) score += 10;
        
        return Math.max(0, Math.min(100, score));
    }

    /**
     * 获取信用等级
     */
    public String getCreditLevel(int score) {
        if (score >= 850) return "AAA";
        if (score >= 800) return "AA";
        if (score >= 750) return "A";
        if (score >= 700) return "BBB";
        if (score >= 650) return "BB";
        if (score >= 600) return "B";
        if (score >= 550) return "CCC";
        if (score >= 500) return "CC";
        return "C";
    }
}