package com.young.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.young.common.PageQuery;
import com.young.common.PageResult;
import com.young.common.Result;
import com.young.pojo.RepaymentPlan;
import com.young.service.RepaymentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "还款计划管理")
@RestController
@RequestMapping("/api/repayment")
public class RepaymentPlanController {

    @Autowired
    private RepaymentPlanService repaymentService;

    /**
     * [客户端] 获取我的账单（分页）
     */
    @Operation(summary = "查询我的还款计划")
    @GetMapping("/my-plans")
    public Result<PageResult<RepaymentPlan>> getMyPlans(
            @RequestAttribute("userId") Long userId,
            @RequestParam(required = false) Integer status,
            PageQuery pageQuery) {
        pageQuery.normalize();
        PageResult<RepaymentPlan> page = repaymentService.getUserPlans(userId, status, pageQuery);
        return Result.success(page);
    }

    /**
     * [客户端] 获取我的账单（不分页，用于内部逻辑）
     */
    @Operation(summary = "查询我的还款计划（全部）")
    @GetMapping("/my-plans/all")
    public Result<List<RepaymentPlan>> getMyPlansAll(
            @RequestAttribute("userId") Long userId,
            @RequestParam(required = false) Integer status) {
        List<RepaymentPlan> list = repaymentService.getUserPlans(userId, status);
        return Result.success(list);
    }

    /**
     * [客户端] 支付某一期分期账单 (正常/逾期清欠)
     */
    @Operation(summary = "支付指定期还款账单")
    @PostMapping("/pay")
    public Result<?> payInstallment(@RequestAttribute("userId") Long userId,
                                    @RequestParam Long planId,
                                    @RequestParam BigDecimal payAmount) {
        repaymentService.payNormalInstallment(userId, planId, payAmount);
        return Result.success("账单支付成功");
    }

    /**
     * [客户端] 提前结清整笔贷款
     */
    @Operation(summary = "提前结清整笔贷款")
    @PostMapping("/pay-early/{loanId}")
    public Result<?> payEarlySettlement(@RequestAttribute("userId") Long userId,
                                        @PathVariable Long loanId) {
        repaymentService.payEarlySettlement(userId, loanId);
        return Result.success("该笔贷款已全部提前结清");
    }
}