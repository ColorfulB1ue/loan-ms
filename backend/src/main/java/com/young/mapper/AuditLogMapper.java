package com.young.mapper;

import com.young.pojo.AuditLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 审计日志Mapper
 */
@Mapper
public interface AuditLogMapper {

    int insert(AuditLog auditLog);

    AuditLog selectById(Long id);

    List<AuditLog> selectByUserId(Long userId);

    List<AuditLog> selectAll();
}