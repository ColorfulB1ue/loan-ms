package com.young.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 贷款申请请求DTO
 */
@Data
@Schema(description = "贷款申请请求")
public class LoanApplyRequest {

    @NotNull(message = "产品ID不能为空")
    @Schema(description = "贷款产品ID")
    private Long productId;

    @NotNull(message = "贷款金额不能为空")
    @DecimalMin(value = "1000", message = "贷款金额最低1000元")
    @Schema(description = "贷款金额", example = "50000")
    private BigDecimal amount;

    @NotNull(message = "贷款期限不能为空")
    @Min(value = 1, message = "贷款期限至少1个月")
    @Schema(description = "贷款期限（月）", example = "12")
    private Integer termMonths;

    @NotBlank(message = "贷款用途不能为空")
    @Size(max = 200, message = "贷款用途最多200个字符")
    @Schema(description = "贷款用途", example = "房屋装修")
    private String purpose;
}