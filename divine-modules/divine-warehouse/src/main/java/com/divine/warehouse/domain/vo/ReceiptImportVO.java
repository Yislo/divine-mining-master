package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrderDetail.class)
public class ReceiptImportVO {

    /**
     * 物品名称
     */
    @ColumnWidth(18)
    @ExcelProperty(value = "物品名称")
    private String itemName;

    /**
     * 规格
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "规格")
    private String skuName;

    /**
     * 单位
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 数量
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "数量")
    private Long quantity;

    /**
     * 单价($)
     */
    @ColumnWidth(16)
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

    /**
     * 货架
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "货架")
    private String storageShelf;

    /**
     * 备注
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "备注")
    private String remark;



}
