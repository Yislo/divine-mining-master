package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ItemSku;
import io.github.linpeilie.annotations.AutoMapper;
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

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 规格id
     */
    @ExcelProperty(value = "规格id")
    private Long itemId;

    /**
     * 规格名称
     */
    @ExcelProperty(value = "规格名称")
    private String skuName;

    /**
     * sku条码
     */
    @ExcelProperty(value = "sku条码")
    private String barcode;

    /**
     * sku编码
     */
    @ExcelProperty(value = "sku编码")
    private String skuNo;

    /**
     * 长(cm)
     */
    @ExcelProperty(value = "长(cm)")
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @ExcelProperty(value = "宽(cm)")
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @ExcelProperty(value = "=高(cm)")
    private BigDecimal height;

    /**
     * 毛重(kg)
     */
    @ExcelProperty(value = "毛重(kg)")
    private BigDecimal grossWeight;

    /**
     * 净重(kg)
     */
    @ExcelProperty(value = "净重(kg)")
    private BigDecimal netWeight;

    /**
     * 单价($)
     */
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;
}
