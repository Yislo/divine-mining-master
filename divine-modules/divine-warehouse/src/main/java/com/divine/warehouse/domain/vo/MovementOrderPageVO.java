package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ExcelIgnoreUnannotated
public class MovementOrderPageVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 源仓库id
     */
    private Long sourceWarehouseId;
    /**
     * 源仓库名称
     */
    private String sourceWarehouseName;
    /**
     * 目标仓库id
     */
    private Long targetWarehouseId;
    /**
     * 目标仓库名称
     */
    private String targetWarehouseName;
    /**
     * 数量
     */
    private Integer totalQuantity;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private  LocalDateTime updateTime;
    /**
     * 操作人
     */
    private String createBy;
    /**
     * 备注
     */
    private String remark;
}
