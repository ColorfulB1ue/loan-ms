package com.young.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数基类
 */
@Data
@Schema(description = "分页查询参数")
public class PageQuery implements Serializable {

    @Schema(description = "当前页码，从1开始", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小，默认10，最大100", example = "10")
    private Integer pageSize = 10;

    /**
     * 校验并修正分页参数
     */
    public void normalize() {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
    }
}
