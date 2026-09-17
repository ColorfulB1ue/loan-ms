package com.young.risk;

import com.young.mapper.LoanApplicationMapper;
import com.young.mapper.LoanProductMapper;
import com.young.mapper.RepaymentPlanMapper;
import com.young.mapper.UserCreditMapper;
import com.young.mapper.UserProfileMapper;
import com.young.pojo.LoanApplication;
import com.young.pojo.LoanProduct;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.UserCredit;
import com.young.pojo.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 风险评估服务
 * 整合信用评分和规则引擎，提供完整的风险评估
 */
@Service
public class RiskAssessmentService {

    private static final Logger log = LoggerFactory.getLogger(RiskAssessmentService.class);

    @Autowired
    private CreditScoreCalculator creditScoreCalculator;
    
    @Autowired
    private RiskRuleEngine riskRuleEngine;
    
    @Autowired
    private UserProfileMapper userProfileMapper;
    
    @Autowired
    private LoanApplicationMapper loanApplicationMapper;
    
    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;
    
    @Autowired
    private UserCreditMapper userCreditMapper;

    /**
     * 执行完整的风险评估
     */
    public RiskAssessment assess(Long userId, LoanApplication application) {
        log.info("[风控评估] 开始评估用户 {} 的贷款申请", userId);
        
        // 1. 获取用户资料
        UserProfile profile = userProfileMapper.selectByUserId(userId);
        
        // 2. 获取用户贷款历史
        List<LoanApplication> loans = loanApplicationMapper.selectList(userId);
        int existingLoanCount = (int) loans.stream()
                .filter(l -> l.getStatus() == 1) // 统计已放款未结清的
                .count();
        
        // 3. 获取还款记录
        List<RepaymentPlan> plans = repaymentPlanMapper.selectByUserId(userId, null);
        
        // 4. 获取授信信息
        UserCredit credit = userCreditMapper.selectByUserId(userId);
        BigDecimal totalCredit = credit != null ? credit.getTotalCredit() : BigDecimal.ZERO;
        BigDecimal usedCredit = credit != null ? credit.getUsedCredit() : BigDecimal.ZERO;
        
        // 5. 计算信用评分
        int creditScore = creditScoreCalculator.calculate(profile, loans, plans, totalCredit, usedCredit);
        String creditLevel = creditScoreCalculator.getCreditLevel(creditScore);
        
        // 6. 执行规则引擎
        RiskRuleEngine.RiskResult ruleResult = riskRuleEngine.evaluate(profile, application, creditScore, existingLoanCount);
        
        // 7. 生成评估结果
        RiskAssessment assessment = new RiskAssessment();
        assessment.setUserId(userId);
        assessment.setCreditScore(creditScore);
        assessment.setCreditLevel(creditLevel);
        assessment.setRiskLevel(ruleResult.getLevel());
        assessment.setRiskReasons(ruleResult.getReasons());
        assessment.setAutoApprove(ruleResult.canAutoApprove());
        assessment.setNeedManualReview(ruleResult.needManualReview());
        
        log.info("[风控评估] 用户 {} 评估完成: 评分={}, 等级={}, 风险等级={}, 可自动审批={}", 
                userId, creditScore, creditLevel, ruleResult.getLevel(), ruleResult.canAutoApprove());
        
        return assessment;
    }

    /**
     * 风险评估结果
     */
    public static class RiskAssessment {
        private Long userId;
        private int creditScore;
        private String creditLevel;
        private RiskRuleEngine.RiskLevel riskLevel;
        private List<String> riskReasons;
        private boolean autoApprove;
        private boolean needManualReview;

        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public int getCreditScore() { return creditScore; }
        public void setCreditScore(int creditScore) { this.creditScore = creditScore; }
        
        public String getCreditLevel() { return creditLevel; }
        public void setCreditLevel(String creditLevel) { this.creditLevel = creditLevel; }
        
        public RiskRuleEngine.RiskLevel getRiskLevel() { return riskLevel; }
        public void setRiskLevel(RiskRuleEngine.RiskLevel riskLevel) { this.riskLevel = riskLevel; }
        
        public List<String> getRiskReasons() { return riskReasons; }
        public void setRiskReasons(List<String> riskReasons) { this.riskReasons = riskReasons; }
        
        public boolean isAutoApprove() { return autoApprove; }
        public void setAutoApprove(boolean autoApprove) { this.autoApprove = autoApprove; }
        
        public boolean isNeedManualReview() { return needManualReview; }
        public void setNeedManualReview(boolean needManualReview) { this.needManualReview = needManualReview; }
    }
}