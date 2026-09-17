package com.young.service;

import com.young.mapper.LoanApplicationMapper;
import com.young.mapper.RepaymentPlanMapper;
import com.young.mapper.RepaymentRecordMapper;
import com.young.pojo.LoanApplication;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.RepaymentRecord;
import com.young.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据导出服务
 */
@Service
public class ExportService {

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;
    
    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;
    
    @Autowired
    private RepaymentRecordMapper repaymentRecordMapper;

    /**
     * 导出贷款记录
     */
    public void exportLoanApplications(Long userId, OutputStream outputStream) throws IOException {
        List<LoanApplication> loans = loanApplicationMapper.selectList(userId);
        
        // 转换为导出格式
        List<LoanExportVO> exportData = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (LoanApplication loan : loans) {
            LoanExportVO vo = new LoanExportVO();
            vo.setId(loan.getId());
            vo.setUserId(loan.getUserId());
            vo.setProductId(loan.getProductId());
            vo.setAmount(loan.getAmount());
            vo.setTermMonths(loan.getTermMonths());
            vo.setAnnualRate(loan.getAnnualRate());
            vo.setPurpose(loan.getPurpose());
            vo.setStatus(getLoanStatusText(loan.getStatus()));
            vo.setApplyTime(loan.getApplyTime() != null ? sdf.format(loan.getApplyTime()) : "");
            vo.setAuditTime(loan.getAuditTime() != null ? sdf.format(loan.getAuditTime()) : "");
            exportData.add(vo);
        }
        
        String[] headers = {"申请ID", "用户ID", "产品ID", "贷款金额", "期限(月)", "年利率", "用途", "状态", "申请时间", "审批时间"};
        ExcelUtils.export("贷款记录", headers, exportData, outputStream);
    }

    /**
     * 导出还款计划
     */
    public void exportRepaymentPlans(Long userId, OutputStream outputStream) throws IOException {
        List<RepaymentPlan> plans = repaymentPlanMapper.selectByUserId(userId, null);
        
        // 转换为导出格式
        List<PlanExportVO> exportData = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (RepaymentPlan plan : plans) {
            PlanExportVO vo = new PlanExportVO();
            vo.setId(plan.getId());
            vo.setLoanId(plan.getLoanId());
            vo.setTermIndex(plan.getTermIndex());
            vo.setPrincipal(plan.getPrincipal());
            vo.setInterest(plan.getInterest());
            vo.setPenalty(plan.getPenalty());
            vo.setTotalAmount(plan.getTotalAmount());
            vo.setDueDate(plan.getDueDate() != null ? sdf.format(plan.getDueDate()) : "");
            vo.setStatus(getPlanStatusText(plan.getStatus()));
            exportData.add(vo);
        }
        
        String[] headers = {"计划ID", "贷款ID", "期数", "本金", "利息", "罚息", "应还总额", "到期日", "状态"};
        ExcelUtils.export("还款计划", headers, exportData, outputStream);
    }

    /**
     * 导出还款记录
     */
    public void exportRepaymentRecords(Long userId, OutputStream outputStream) throws IOException {
        List<RepaymentRecord> records = repaymentRecordMapper.selectByUserId(userId);
        
        // 转换为导出格式
        List<RecordExportVO> exportData = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (RepaymentRecord record : records) {
            RecordExportVO vo = new RecordExportVO();
            vo.setId(record.getId());
            vo.setPlanId(record.getPlanId());
            vo.setLoanId(record.getLoanId());
            vo.setPayAmount(record.getPayAmount());
            vo.setPayType(getPayTypeText(record.getPayType()));
            vo.setPayTime(record.getPayTime() != null ? sdf.format(record.getPayTime()) : "");
            vo.setRemark(record.getRemark());
            exportData.add(vo);
        }
        
        String[] headers = {"记录ID", "计划ID", "贷款ID", "支付金额", "支付类型", "支付时间", "备注"};
        ExcelUtils.export("还款记录", headers, exportData, outputStream);
    }

    private String getLoanStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审批";
            case 1: return "已放款";
            case 2: return "已驳回";
            case 3: return "已结清";
            default: return "未知";
        }
    }

    private String getPlanStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待还";
            case 1: return "已还清";
            case 2: return "逾期中";
            case 3: return "提前结清";
            default: return "未知";
        }
    }

    private String getPayTypeText(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 1: return "正常还款";
            case 2: return "逾期还款";
            case 3: return "提前结清";
            default: return "未知";
        }
    }

    // 导出VO类
    public static class LoanExportVO {
        private Long id;
        private Long userId;
        private Long productId;
        private java.math.BigDecimal amount;
        private Integer termMonths;
        private java.math.BigDecimal annualRate;
        private String purpose;
        private String status;
        private String applyTime;
        private String auditTime;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public java.math.BigDecimal getAmount() { return amount; }
        public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
        public Integer getTermMonths() { return termMonths; }
        public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
        public java.math.BigDecimal getAnnualRate() { return annualRate; }
        public void setAnnualRate(java.math.BigDecimal annualRate) { this.annualRate = annualRate; }
        public String getPurpose() { return purpose; }
        public void setPurpose(String purpose) { this.purpose = purpose; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getApplyTime() { return applyTime; }
        public void setApplyTime(String applyTime) { this.applyTime = applyTime; }
        public String getAuditTime() { return auditTime; }
        public void setAuditTime(String auditTime) { this.auditTime = auditTime; }
    }

    public static class PlanExportVO {
        private Long id;
        private Long loanId;
        private Integer termIndex;
        private java.math.BigDecimal principal;
        private java.math.BigDecimal interest;
        private java.math.BigDecimal penalty;
        private java.math.BigDecimal totalAmount;
        private String dueDate;
        private String status;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getLoanId() { return loanId; }
        public void setLoanId(Long loanId) { this.loanId = loanId; }
        public Integer getTermIndex() { return termIndex; }
        public void setTermIndex(Integer termIndex) { this.termIndex = termIndex; }
        public java.math.BigDecimal getPrincipal() { return principal; }
        public void setPrincipal(java.math.BigDecimal principal) { this.principal = principal; }
        public java.math.BigDecimal getInterest() { return interest; }
        public void setInterest(java.math.BigDecimal interest) { this.interest = interest; }
        public java.math.BigDecimal getPenalty() { return penalty; }
        public void setPenalty(java.math.BigDecimal penalty) { this.penalty = penalty; }
        public java.math.BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class RecordExportVO {
        private Long id;
        private Long planId;
        private Long loanId;
        private java.math.BigDecimal payAmount;
        private String payType;
        private String payTime;
        private String remark;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getPlanId() { return planId; }
        public void setPlanId(Long planId) { this.planId = planId; }
        public Long getLoanId() { return loanId; }
        public void setLoanId(Long loanId) { this.loanId = loanId; }
        public java.math.BigDecimal getPayAmount() { return payAmount; }
        public void setPayAmount(java.math.BigDecimal payAmount) { this.payAmount = payAmount; }
        public String getPayType() { return payType; }
        public void setPayType(String payType) { this.payType = payType; }
        public String getPayTime() { return payTime; }
        public void setPayTime(String payTime) { this.payTime = payTime; }
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
    }
}