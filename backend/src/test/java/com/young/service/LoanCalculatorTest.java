package com.young.service;

import com.young.pojo.RepaymentPlan;
import com.young.utils.LoanCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 贷款计算器测试
 */
class LoanCalculatorTest {

    private LoanCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new LoanCalculator();
    }

    @Test
    void testGenerateEqualInstallmentPlan() {
        BigDecimal amount = new BigDecimal("10000");
        int termMonths = 3;
        BigDecimal annualRate = new BigDecimal("0.048"); // 4.8%
        Date startDate = new Date();

        List<RepaymentPlan> plans = calculator.generateEqualInstallmentPlan(
                1L, 1L, amount, termMonths, annualRate, startDate);

        assertNotNull(plans);
        assertEquals(3, plans.size());

        // 验证每期计划
        BigDecimal totalPrincipal = BigDecimal.ZERO;
        for (RepaymentPlan plan : plans) {
            assertNotNull(plan.getPrincipal());
            assertNotNull(plan.getInterest());
            assertNotNull(plan.getTotalAmount());
            assertNotNull(plan.getDueDate());

            // 本金应大于0
            assertTrue(plan.getPrincipal().compareTo(BigDecimal.ZERO) > 0);
            // 利息应大于等于0
            assertTrue(plan.getInterest().compareTo(BigDecimal.ZERO) >= 0);
            // 总额 = 本金 + 利息
            BigDecimal expectedTotal = plan.getPrincipal().add(plan.getInterest());
            assertEquals(0, expectedTotal.compareTo(plan.getTotalAmount().setScale(2, RoundingMode.HALF_UP)));

            totalPrincipal = totalPrincipal.add(plan.getPrincipal());
        }

        // 总本金应等于贷款金额（允许小误差）
        BigDecimal diff = totalPrincipal.subtract(amount).abs();
        assertTrue(diff.compareTo(new BigDecimal("0.01")) <= 0,
                "总本金应约等于贷款金额，差额: " + diff);
    }

    @Test
    void testGeneratePlanWithZeroRate() {
        BigDecimal amount = new BigDecimal("10000");
        int termMonths = 2;
        BigDecimal annualRate = BigDecimal.ZERO;
        Date startDate = new Date();

        List<RepaymentPlan> plans = calculator.generateEqualInstallmentPlan(
                1L, 1L, amount, termMonths, annualRate, startDate);

        assertNotNull(plans);
        assertEquals(2, plans.size());

        // 零利率时，利息应为0
        for (RepaymentPlan plan : plans) {
            assertEquals(0, BigDecimal.ZERO.compareTo(plan.getInterest()));
            assertEquals(0, plan.getPrincipal().compareTo(plan.getTotalAmount()));
        }
    }

    @Test
    void testGeneratePlanWithSingleTerm() {
        BigDecimal amount = new BigDecimal("5000");
        int termMonths = 1;
        BigDecimal annualRate = new BigDecimal("0.06");
        Date startDate = new Date();

        List<RepaymentPlan> plans = calculator.generateEqualInstallmentPlan(
                1L, 1L, amount, termMonths, annualRate, startDate);

        assertNotNull(plans);
        assertEquals(1, plans.size());

        // 单期时，本金应等于贷款金额
        assertEquals(0, amount.compareTo(plans.get(0).getPrincipal()));
    }

    @Test
    void testGeneratePlanWithLargeAmount() {
        BigDecimal amount = new BigDecimal("1000000");
        int termMonths = 12;
        BigDecimal annualRate = new BigDecimal("0.036");
        Date startDate = new Date();

        List<RepaymentPlan> plans = calculator.generateEqualInstallmentPlan(
                1L, 1L, amount, termMonths, annualRate, startDate);

        assertNotNull(plans);
        assertEquals(12, plans.size());

        // 验证每期都有合理的值
        for (RepaymentPlan plan : plans) {
            assertTrue(plan.getPrincipal().compareTo(BigDecimal.ZERO) > 0);
            assertTrue(plan.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
        }
    }
}