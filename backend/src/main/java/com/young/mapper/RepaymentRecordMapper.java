package com.young.mapper;

import com.young.pojo.RepaymentRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RepaymentRecordMapper {
    int insert(RepaymentRecord record);

    /** 【管理端】查询全平台所有入账历史 */
    @Select("SELECT * FROM repayment_record ORDER BY pay_time DESC")
    List<RepaymentRecord> selectAll();

    /** 查询指定用户的还款记录 */
    @Select("SELECT * FROM repayment_record WHERE user_id = #{userId} ORDER BY pay_time DESC")
    List<RepaymentRecord> selectByUserId(Long userId);
}
