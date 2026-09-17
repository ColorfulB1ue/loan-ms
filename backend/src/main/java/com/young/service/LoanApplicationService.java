package com.young.service;

import com.young.common.PageQuery;
import com.young.common.PageResult;
import com.young.pojo.LoanApplication;

import java.util.List;

public interface LoanApplicationService {

    /**
     * 客户发起贷款申请，冻结可用额度
     */
    void applyLoan(Long userId, LoanApplication application);

    /**
     * 管理员审批通过并放款（生成等额本息还款计划）
     */
    void approveAndDisburse(Long applicationId);

    /**
     * 管理员驳回申请，解冻额度
     */
    void rejectLoan(Long applicationId);

    /**
     * 分页查询贷款清单 (userId为空则查全部)
     */
    PageResult<LoanApplication> getApplicationList(Long userId, PageQuery pageQuery);

    /**
     * 查询全部待审批贷款申请（管理端，分页）
     */
    PageResult<LoanApplication> getPendingList(PageQuery pageQuery);

    /**
     * 查询贷款清单（不分页，用于内部逻辑）
     */
    List<LoanApplication> getApplicationList(Long userId);
}
