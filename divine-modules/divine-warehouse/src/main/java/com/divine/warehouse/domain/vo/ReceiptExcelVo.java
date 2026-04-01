package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.common.excel.annotation.ExcelDictFormat;
import com.divine.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库单视图对象 wms_receipt_order
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
public class ReceiptExcelVo {

    @ColumnWidth(15)
    @ExcelProperty(value = "入库单号")
    private String receiptNo;

    @ColumnWidth(12)
    @ExcelProperty(value = "所属仓库")
    private String warehouseName;

    @ColumnWidth(20)
    @ExcelProperty(value = "物品名称")
    private String itemName;

    @ColumnWidth(20)
    @ExcelProperty(value = "规格名称")
    private String skuName;

    @ColumnWidth(12)
    @ExcelProperty(value = "数量")
    private Long quantity;

    @ColumnWidth(12)
    @ExcelProperty(value = "单位")
    private String unit;

    @ColumnWidth(12)
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

    @ColumnWidth(12)
    @ExcelProperty(value = "总价($)")
    private BigDecimal totalPrice;

    @ColumnWidth(20)
    @ExcelProperty(value = "入库时间")
    private LocalDateTime createTime;

    @ColumnWidth(12)
    @ExcelProperty(value = "入库状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "wms_receipt_status")
    private Integer receiptStatus;

    @ColumnWidth(12)
    @ExcelProperty(value = "入库类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "wms_receipt_type")
    private Integer optType;

    @ColumnWidth(12)
    @ExcelProperty(value = "货架")
    private String storageShelf;
}
