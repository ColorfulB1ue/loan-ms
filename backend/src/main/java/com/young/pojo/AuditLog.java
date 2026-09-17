package com.young.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 审计日志实体
 */
@Data
public class AuditLog {
    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String module;
    private String targetId;
    private String targetType;
    private String detail;
    private String ip;
    private Date createTime;
}