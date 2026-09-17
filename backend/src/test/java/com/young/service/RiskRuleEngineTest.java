package com.young.service;

import com.young.pojo.LoanApplication;
import com.young.pojo.UserProfile;
import com.young.risk.RiskRuleEngine;
import com.young.risk.RiskRuleEngine.RiskLevel;
import com.young.risk.RiskRuleEngine.RiskResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 风控规则引擎测试
 */
class RiskRuleEngineTest {

    private RiskRuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        ruleEngine = new RiskRuleEngine();
    }

    @Test
    void testLowRiskScenario() {
        // 低风险场景：良好KYC、高信用评分、小额贷款
        UserProfile profile = createApprovedProfile();
        LoanApplication application = createNormalApplication();
        int creditScore = 800;
        int existingLoanCount = 0;

        RiskResult result = ruleEngine.evaluate(profile, application, creditScore, existingLoanCount);

        assertEquals(RiskLevel.LOW, result.getLevel());
        assertTrue(result.canAutoApprove());
        assertFalse(result.needManualReview());
    }

    @Test
    void testRejectDueToKyc() {
        // KYC未通过应直接拒绝
        UserProfile profile = new UserProfile();
        profile.setStatus(0); // 待审核
        LoanApplication application = createNormalApplication();

        RiskResult result = ruleEngine.evaluate(profile, application, 800, 0);

        assertEquals(RiskLevel.REJECT, result.getLevel());
        assertTrue(result.shouldReject());
        assertTrue(result.getReasons().contains("实名认证未通过"));
    }

    @Test
    void testMediumRiskDueToAmount() {
        // 大额贷款应触发中风险
        UserProfile profile = createApprovedProfile();
        LoanApplication application = new LoanApplication();
        application.setAmount(new BigDecimal("300000")); // 30万
        application.setTermMonths(12);

        RiskResult result = ruleEngine.evaluate(profile, application, 750, 0);

        assertTrue(result.getLevel() == RiskLevel.MEDIUM || result.getLevel() == RiskLevel.HIGH);
        assertTrue(result.needManualReview());
    }

    @Test
    void testHighRiskDueToMultipleLoans() {
        // 多头借贷应触发高风险
        UserProfile profile = createApprovedProfile();
        LoanApplication application = createNormalApplication();
        int existingLoanCount = 3; // 已有3笔贷款

        RiskResult result = ruleEngine.evaluate(profile, application, 700, existingLoanCount);

        assertEquals(RiskLevel.HIGH, result.getLevel());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("多头借贷")));
    }

    @Test
    void testHighRiskDueToLowScore() {
        // 低信用评分应触发高风险
        UserProfile profile = createApprovedProfile();
        LoanApplication application = createNormalApplication();
        int creditScore = 450;

        RiskResult result = ruleEngine.evaluate(profile, application, creditScore, 0);

        assertEquals(RiskLevel.HIGH, result.getLevel());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("信用评分过低")));
    }

    @Test
    void testRejectDueToNullProfile() {
        // 空资料应拒绝
        LoanApplication application = createNormalApplication();

        RiskResult result = ruleEngine.evaluate(null, application, 800, 0);

        assertEquals(RiskLevel.REJECT, result.getLevel());
    }

    @Test
    void testMediumRiskDueToLongTerm() {
        // 长期限应触发中风险
        UserProfile profile = createApprovedProfile();
        LoanApplication application = new LoanApplication();
        application.setAmount(new BigDecimal("50000"));
        application.setTermMonths(48); // 48个月

        RiskResult result = ruleEngine.evaluate(profile, application, 700, 0);

        assertTrue(result.getLevel() != RiskLevel.LOW);
    }

    // 辅助方法
    private UserProfile createApprovedProfile() {
        UserProfile profile = new UserProfile();
        profile.setStatus(1); // 审核通过
        profile.setIdCard("11010519990101888X");
        return profile;
    }

    private LoanApplication createNormalApplication() {
        LoanApplication app = new LoanApplication();
        app.setAmount(new BigDecimal("50000"));
        app.setTermMonths(12);
        return app;
    }
}