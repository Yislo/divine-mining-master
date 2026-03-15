package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ExcelIgnoreUnannotated
public class BaseOrderDetailVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 出、入、盘、移库单详情id
     */
    @ExcelProperty(value = "业务单详情id")
    private Long id;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long skuId;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private Long quantity;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private BigDecimal unitPrice;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "所属仓库")
    private Long warehouseId;

    /**
     * 所属货架
     */
    @ExcelProperty(value = "所属货架")
    private String storageShelf;

    /**
     * 图片信息
     */
    private List<String> fileList;

    /**
     * 规格信息
     */
    private ItemSkuVo itemSku;

    /**
     * 物品信息
     */
    private ItemVo item;
}
