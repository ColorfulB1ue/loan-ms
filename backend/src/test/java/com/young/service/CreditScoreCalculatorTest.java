package com.young.service;

import com.young.pojo.LoanApplication;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.UserProfile;
import com.young.risk.CreditScoreCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 信用评分计算器测试
 */
class CreditScoreCalculatorTest {

    private CreditScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CreditScoreCalculator();
    }

    @Test
    void testCalculateWithCompleteProfile() {
        // 准备完整的用户资料
        UserProfile profile = createCompleteProfile();
        List<LoanApplication> loans = createGoodLoanHistory();
        List<RepaymentPlan> plans = createGoodRepaymentHistory();
        BigDecimal totalCredit = new BigDecimal("100000");
        BigDecimal usedCredit = new BigDecimal("30000");

        int score = calculator.calculate(profile, loans, plans, totalCredit, usedCredit);

        // 完整资料+良好还款记录应该有较高分数
        assertTrue(score >= 700, "完整资料和良好记录的评分应>=700，实际: " + score);
        assertTrue(score <= 950, "评分不应超过950");
    }

    @Test
    void testCalculateWithMinimalProfile() {
        // 准备最小化的用户资料
        UserProfile profile = new UserProfile();
        profile.setRealName("张三");
        profile.setIdCard("11010519990101888X");

        List<LoanApplication> loans = new ArrayList<>();
        List<RepaymentPlan> plans = new ArrayList<>();
        BigDecimal totalCredit = new BigDecimal("50000");
        BigDecimal usedCredit = BigDecimal.ZERO;

        int score = calculator.calculate(profile, loans, plans, totalCredit, usedCredit);

        // 最小化资料应该有中等分数
        assertTrue(score >= 600, "最小化资料的评分应>=600，实际: " + score);
        assertTrue(score <= 800, "最小化资料的评分应<=800");
    }

    @Test
    void testCalculateWithNullProfile() {
        int score = calculator.calculate(null, new ArrayList<>(), new ArrayList<>(), 
                                         BigDecimal.ZERO, BigDecimal.ZERO);

        // 空资料应该有较低分数
        assertTrue(score >= 300, "空资料评分应>=300");
        assertTrue(score <= 600, "空资料评分应<=600");
    }

    @Test
    void testCalculateWithOverdueHistory() {
        UserProfile profile = createCompleteProfile();
        List<LoanApplication> loans = createGoodLoanHistory();
        List<RepaymentPlan> plans = createOverdueRepaymentHistory();
        BigDecimal totalCredit = new BigDecimal("100000");
        BigDecimal usedCredit = new BigDecimal("80000");

        int score = calculator.calculate(profile, loans, plans, totalCredit, usedCredit);

        // 逾期记录+高负债率应该降低分数
        assertTrue(score < 700, "逾期记录应降低评分，实际: " + score);
    }

    @Test
    void testGetCreditLevel() {
        assertEquals("AAA", calculator.getCreditLevel(900));
        assertEquals("AA", calculator.getCreditLevel(850));
        assertEquals("A", calculator.getCreditLevel(800));
        assertEquals("BBB", calculator.getCreditLevel(750));
        assertEquals("BB", calculator.getCreditLevel(700));
        assertEquals("B", calculator.getCreditLevel(650));
        assertEquals("CCC", calculator.getCreditLevel(600));
        assertEquals("CC", calculator.getCreditLevel(550));
        assertEquals("C", calculator.getCreditLevel(500));
    }

    @Test
    void testScoreRange() {
        // 测试极端情况，分数应该在300-950范围内
        UserProfile profile = createCompleteProfile();
        List<LoanApplication> loans = createGoodLoanHistory();
        List<RepaymentPlan> plans = createGoodRepaymentHistory();

        // 极低负债
        int score1 = calculator.calculate(profile, loans, plans, 
                                          new BigDecimal("1000000"), BigDecimal.ZERO);
        assertTrue(score1 >= 300 && score1 <= 950);

        // 极高负债
        int score2 = calculator.calculate(profile, loans, plans, 
                                          new BigDecimal("10000"), new BigDecimal("10000"));
        assertTrue(score2 >= 300 && score2 <= 950);
    }

    // 辅助方法
    private UserProfile createCompleteProfile() {
        UserProfile profile = new UserProfile();
        profile.setRealName("张三");
        profile.setIdCard("11010519990101888X");
        profile.setPhone("13800138000");
        profile.setEmail("zhangsan@example.com");
        profile.setBankCard("6227000012345678");
        profile.setBankName("中国建设银行");
        profile.setIdCardFront("front.jpg");
        profile.setIdCardBack("back.jpg");
        return profile;
    }

    private List<LoanApplication> createGoodLoanHistory() {
        List<LoanApplication> loans = new ArrayList<>();
        LoanApplication loan = new LoanApplication();
        loan.setId(1L);
        loan.setStatus(3); // 已结清
        loan.setAmount(new BigDecimal("10000"));
        loans.add(loan);
        return loans;
    }

    private List<RepaymentPlan> createGoodRepaymentHistory() {
        List<RepaymentPlan> plans = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            RepaymentPlan plan = new RepaymentPlan();
            plan.setId((long) i);
            plan.setStatus(1); // 已还清
            plan.setDueDate(new Date());
            plans.add(plan);
        }
        return plans;
    }

    private List<RepaymentPlan> createOverdueRepaymentHistory() {
        List<RepaymentPlan> plans = new ArrayList<>();
        RepaymentPlan plan1 = new RepaymentPlan();
        plan1.setId(1L);
        plan1.setStatus(1); // 已还清
        plans.add(plan1);

        RepaymentPlan plan2 = new RepaymentPlan();
        plan2.setId(2L);
        plan2.setStatus(2); // 逾期
        plans.add(plan2);

        RepaymentPlan plan3 = new RepaymentPlan();
        plan3.setId(3L);
        plan3.setStatus(2); // 逾期
        plans.add(plan3);

        return plans;
    }
}