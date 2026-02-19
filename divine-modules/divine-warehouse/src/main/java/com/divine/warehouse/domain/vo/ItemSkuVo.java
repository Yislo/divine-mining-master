package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ItemSku;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;



@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ItemSku.class)
public class ItemSkuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "id")
    @Schema(description = "id")
    private Long id;

    @Schema(description = "规格id")
    @ExcelProperty(value = "规格id")
    private Long itemId;

    @ExcelProperty(value = "规格名称")
    @Schema(description = "规格名称")
    private String skuName;

    @Schema(description = "sku条码")
    @ExcelProperty(value = "sku条码")
    private String barcode;

    @Schema(description = "sku编码")
    @ExcelProperty(value = "sku编码")
    private String skuNo;

    @Schema(description = "长(cm)")
    @ExcelProperty(value = "长(cm)")
    private BigDecimal length;

    @Schema(description = "宽(cm)")
    @ExcelProperty(value = "宽(cm)")
    private BigDecimal width;

    @Schema(description = "高(cm)")
    @ExcelProperty(value = "=高(cm)")
    private BigDecimal height;

    @Schema(description = "毛重(kg)")
    @ExcelProperty(value = "毛重(kg)")
    private BigDecimal grossWeight;

    @Schema(description = "净重(kg)")
    @ExcelProperty(value = "净重(kg)")
    private BigDecimal netWeight;

    @Schema(description = "单价($)")
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

    @Schema(description = "所属货架")
    @ExcelProperty(value = "所属货架")
    private String storageShelf;
}
