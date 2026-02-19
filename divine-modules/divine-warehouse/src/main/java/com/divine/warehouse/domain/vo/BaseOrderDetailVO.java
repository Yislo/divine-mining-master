package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ExcelIgnoreUnannotated
public class BaseOrderDetailVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "出、入、盘、移库id")
    private Long id;

    @ExcelProperty(value = "出、入、盘、移库编号")
    private String orderNo;

    @ExcelProperty(value = "规格id")
    private Long skuId;

    @ExcelProperty(value = "数量")
    private BigDecimal quantity;

    @ExcelProperty(value = "金额")
    private BigDecimal amount;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "所属仓库")
    private Long warehouseId;

    private ItemSkuVo itemSku;

    private ItemVo item;
}
