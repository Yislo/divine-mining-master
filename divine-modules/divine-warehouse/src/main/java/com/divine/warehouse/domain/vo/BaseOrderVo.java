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

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "订单号")
    private String orderNo;

    @ExcelProperty(value = "物品总数")
    private BigDecimal totalQuantity;

    @ExcelProperty(value = "总金额")
    private BigDecimal totalPrice;

    @ExcelProperty(value = "状态")
    private Integer orderStatus;

    @ExcelProperty(value = "仓库id")
    private Long warehouseId;

    @ExcelProperty(value = "备注")
    private String remark;

    private List<T> details;
}
