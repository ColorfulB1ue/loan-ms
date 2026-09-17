package com.young.common;

import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装类
 */
@Data
@Schema(description = "分页查询结果")
public class PageResult<T> implements Serializable {

    @Schema(description = "当前页码")
    private int pageNum;

    @Schema(description = "每页大小")
    private int pageSize;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "总页数")
    private int pages;

    @Schema(description = "数据列表")
    private List<T> list;

    /**
     * 从 PageHelper 的 PageInfo 构建分页结果
     */
    public static <T> PageResult<T> of(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        result.setList(pageInfo.getList());
        return result;
    }

    /**
     * 手动构建分页结果
     */
    public static <T> PageResult<T> of(List<T> list, int pageNum, int pageSize, long total) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / pageSize));
        result.setList(list);
        return result;
    }
}
