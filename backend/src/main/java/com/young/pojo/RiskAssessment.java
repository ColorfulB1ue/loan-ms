package com.young.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 风险评估记录实体
 */
@Data
public class RiskAssessment {
    private Long id;
    private Long userId;
    private Long loanId;
    private Integer creditScore;
    private String creditLevel;
    private String riskLevel;
    private String riskReasons;
    private Boolean autoApproved;
    private Date createTime;
}