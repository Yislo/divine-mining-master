package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/12 19:51
 */
@Data
public class BoardListVO {

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    private String warehouseName;

    /**
     * 物品id
     */
    @ExcelProperty(value = "物品id")
    private Long itemId;

    /**
     * 物品名称
     */
    @ExcelProperty(value = "物品名称")
    private String itemName;

    /**
     * 物品编号
     */
    @ExcelProperty(value = "物品编号")
    private String itemNo;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * sku编号
     */
    private String skuNo;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 货架
     */
    private String storageShelf;

    /**
     * 库存
     */
    private Long quantity;


}
