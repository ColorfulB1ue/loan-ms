package com.young.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户实名认证资料实体
 */
@Data
@Schema(description = "用户实名认证资料")
public class UserProfile {

    @Schema(description = "档案ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "身份证正面照URL")
    private String idCardFront;

    @Schema(description = "身份证反面照URL")
    private String idCardBack;

    @Schema(description = "开户银行")
    private String bankName;

    @Schema(description = "银行卡号")
    private String bankCard;

    @Schema(description = "联系手机")
    private String phone;

    @Schema(description = "电子邮箱")
    private String email;

    @Schema(description = "状态：0-待审核, 1-审核通过, 2-驳回")
    private Integer status;

    @Schema(description = "审核时间")
    private Date auditTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "用户名（关联查询）")
    private String username;
}