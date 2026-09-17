package com.young.service;

import com.young.common.PageQuery;
import com.young.common.PageResult;
import com.young.pojo.RepaymentPlan;

import java.math.BigDecimal;
import java.util.List;

public interface RepaymentPlanService {

    /**
     * 客户查询自己的账单计划（分页）
     */
    PageResult<RepaymentPlan> getUserPlans(Long userId, Integer status, PageQuery pageQuery);

    /**
     * 客户查询自己的账单计划（不分页）
     */
    List<RepaymentPlan> getUserPlans(Long userId, Integer status);

    /**
     * 客户主动按期结算当期账单
     * 涉及验证金额、消除逾期状态(如存在)、记录流水(RepaymentRecord)、释放 Credit 款项等
     *
     * @param planId    账单ID
     * @param payAmount 客户实际通过第三方支付支付的金额
     */
    void payNormalInstallment(Long userId, Long planId, BigDecimal payAmount);

    /**
     * 客户提前选择结清某笔贷款的所有剩余本金
     */
    void payEarlySettlement(Long userId, Long loanId);
}