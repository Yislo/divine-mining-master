package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.common.mybatis.core.domain.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
public class BaseOrderVo<T extends BaseOrderDetailVO> extends BaseVo {

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 物品总数
     */
    @ExcelProperty(value = "物品总数")
    private Long totalQuantity;

    /**
     * 总金额
     */
    @ExcelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /**
     * 总金额
     */
    @ExcelProperty(value = "备注")
    private String remark;

    private List<T> details;
}
