package com.young.controller;

import com.young.common.PageQuery;
import com.young.common.PageResult;
import com.young.common.RequireRole;
import com.young.common.Result;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.RepaymentRecord;
import com.young.service.FinanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "财务分析管理")
@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    /**
     * [财务中心] 查询全平台所有还款计划（含客户姓名，分页）
     */
    @Operation(summary = "查询全平台还款计划")
    @RequireRole
    @GetMapping("/plans")
    public Result<PageResult<RepaymentPlan>> getAllPlans(PageQuery pageQuery) {
        pageQuery.normalize();
        return Result.success(financeService.getAllPlans(pageQuery));
    }

    /**
     * [财务中心] 查询全平台所有历史入账明细（分页）
     */
    @Operation(summary = "查询全平台入账明细")
    @RequireRole
    @GetMapping("/records")
    public Result<PageResult<RepaymentRecord>> getAllRecords(PageQuery pageQuery) {
        pageQuery.normalize();
        return Result.success(financeService.getAllRecords(pageQuery));
    }

    /**
     * 手动触发逾期清算任务
     * （专供具有权限的运营人员应对突发及错账情况的应急干预）
     */
    @Operation(summary = "手动触发逾期清算任务")
    @RequireRole
    @PostMapping("/trigger-overdue")
    public Result<String> triggerOverdue() {
        financeService.triggerOverdueScan();
        return Result.success("逾期清算任务已手动触发，请查看控制台日志");
    }
}