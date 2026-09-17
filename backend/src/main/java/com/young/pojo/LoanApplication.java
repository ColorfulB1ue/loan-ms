package com.young.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 贷款申请实体
 */
@Data
@Schema(description = "贷款申请")
public class LoanApplication {

    @Schema(description = "申请ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "关联产品ID")
    private Long productId;

    @Schema(description = "贷款金额")
    private BigDecimal amount;

    @Schema(description = "贷款期限（月）")
    private Integer termMonths;

    @Schema(description = "年化利率")
    private BigDecimal annualRate;

    @Schema(description = "贷款用途")
    private String purpose;

    @Schema(description = "状态：0-待审批, 1-已放款, 2-驳回, 3-已结清")
    private Integer status;

    @Schema(description = "申请时间")
    private Date applyTime;

    @Schema(description = "审批时间")
    private Date auditTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    // 关联展示字段
    @Schema(description = "用户名（关联查询）")
    private String username;

    @Schema(description = "真实姓名（关联查询）")
    private String realName;

    @Schema(description = "产品名称（关联查询）")
    private String productName;
}