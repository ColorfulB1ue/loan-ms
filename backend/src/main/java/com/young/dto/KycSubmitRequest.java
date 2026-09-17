package com.young.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * KYC实名认证提交请求DTO
 */
@Data
@Schema(description = "KYC实名认证提交请求")
public class KycSubmitRequest {

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "姓名最多50个字符")
    @Schema(description = "真实姓名")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$", 
             message = "身份证号格式不正确")
    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "身份证正面照URL")
    private String idCardFront;

    @Schema(description = "身份证反面照URL")
    private String idCardBack;

    @Size(max = 100, message = "银行名称最多100个字符")
    @Schema(description = "开户银行")
    private String bankName;

    @Pattern(regexp = "^\\d{16,19}$", message = "银行卡号格式不正确")
    @Schema(description = "银行卡号")
    private String bankCard;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系手机")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "电子邮箱")
    private String email;
}