package com.young.service;

import com.young.mapper.AuditLogMapper;
import com.young.pojo.AuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审计日志服务
 */
@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    private AuditLogMapper auditLogMapper;

    /**
     * 异步记录审计日志
     */
    @Async
    public void log(Long userId, String username, String action, String module,
                    String targetId, String targetType, String detail, String ip) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setUserId(userId);
            auditLog.setUsername(username);
            auditLog.setAction(action);
            auditLog.setModule(module);
            auditLog.setTargetId(targetId);
            auditLog.setTargetType(targetType);
            auditLog.setDetail(detail);
            auditLog.setIp(ip);
            auditLogMapper.insert(auditLog);
        } catch (Exception e) {
            log.error("记录审计日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 记录登录日志
     */
    public void logLogin(Long userId, String username, String ip, boolean success) {
        String action = success ? "LOGIN_SUCCESS" : "LOGIN_FAILED";
        String detail = success ? "登录成功" : "登录失败";
        log(userId, username, action, "认证", null, null, detail, ip);
    }

    /**
     * 记录贷款申请
     */
    public void logLoanApply(Long userId, String username, Long loanId, String ip) {
        log(userId, username, "LOAN_APPLY", "贷款", String.valueOf(loanId), "LoanApplication", "提交贷款申请", ip);
    }

    /**
     * 记录贷款审批
     */
    public void logLoanApprove(Long userId, String username, Long loanId, boolean approved, String ip) {
        String action = approved ? "LOAN_APPROVE" : "LOAN_REJECT";
        String detail = approved ? "审批通过并放款" : "审批驳回";
        log(userId, username, action, "贷款", String.valueOf(loanId), "LoanApplication", detail, ip);
    }

    /**
     * 记录还款操作
     */
    public void logRepayment(Long userId, String username, Long planId, String ip) {
        log(userId, username, "REPAYMENT", "还款", String.valueOf(planId), "RepaymentPlan", "还款成功", ip);
    }

    /**
     * 记录KYC审核
     */
    public void logKycAudit(Long userId, String username, Long profileId, boolean approved, String ip) {
        String action = approved ? "KYC_APPROVE" : "KYC_REJECT";
        String detail = approved ? "实名审核通过" : "实名审核驳回";
        log(userId, username, action, "KYC", String.valueOf(profileId), "UserProfile", detail, ip);
    }

    /**
     * 记录密码修改
     */
    public void logPasswordChange(Long userId, String username, String ip) {
        log(userId, username, "PASSWORD_CHANGE", "认证", null, null, "修改密码", ip);
    }

    /**
     * 查询用户的审计日志
     */
    public List<AuditLog> getUserLogs(Long userId) {
        return auditLogMapper.selectByUserId(userId);
    }

    /**
     * 查询所有审计日志
     */
    public List<AuditLog> getAllLogs() {
        return auditLogMapper.selectAll();
    }
}