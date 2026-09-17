package com.young.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款计划实体
 */
@Data
@Schema(description = "还款计划")
public class RepaymentPlan {

    @Schema(description = "计划ID")
    private Long id;

    @Schema(description = "贷款申请ID")
    private Long loanId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "期数")
    private Integer termIndex;

    @Schema(description = "应还本金")
    private BigDecimal principal;

    @Schema(description = "应还利息")
    private BigDecimal interest;

    @Schema(description = "罚息")
    private BigDecimal penalty;

    @Schema(description = "应还总额")
    private BigDecimal totalAmount;

    @Schema(description = "到期日")
    private Date dueDate;

    @Schema(description = "状态：0-待还, 1-已还清, 2-逾期中, 3-提前结清")
    private Integer status;

    @Schema(description = "结清时间")
    private Date settledTime;

    @Schema(description = "用户名（关联查询）")
    private String username;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}