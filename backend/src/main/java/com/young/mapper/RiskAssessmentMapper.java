package com.young.mapper;

import com.young.pojo.RiskAssessment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 风险评估记录Mapper
 */
@Mapper
public interface RiskAssessmentMapper {
    
    int insert(RiskAssessment assessment);
    
    RiskAssessment selectById(Long id);
    
    RiskAssessment selectLatestByUserId(Long userId);
    
    RiskAssessment selectByLoanId(Long loanId);
    
    List<RiskAssessment> selectByUserId(Long userId);
}