package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.MovementOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 移库单视图对象 wms_movement_order
 *
 * @author yisl
 * @date 2024-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = MovementOrder.class)
public class MovementOrderVo extends BaseOrderVo<MovementOrderDetailVO>{

    /**
     * 移库单号
     */
    private String moveNo;

    /**
     * 盘库状态(0:未盘库,1:已盘库,-1:已作废)
     */
    private Integer moveStatus;

    /**
     * 源仓库id
     */
    @ExcelProperty(value = "源仓库id")
    private Long sourceWarehouseId;

    /**
     * 源仓库
     */
    @ExcelProperty(value = "源仓库")
    private String sourceWarehouseName;

    /**
     * 目标仓库id
     */
    @ExcelProperty(value = "目标仓库id")
    private Long targetWarehouseId;

    /**
     * 目标仓库名称
     */
    @ExcelProperty(value = "目标仓库")
    private String targetWarehouseName;
}
