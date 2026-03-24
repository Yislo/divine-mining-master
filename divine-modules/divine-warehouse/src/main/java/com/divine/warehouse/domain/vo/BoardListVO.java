package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/12 19:51
 */
@Data
@ExcelIgnoreUnannotated
public class BoardListVO {

    /**
     * 库存id
     */
    private Long id;

    /**
     * 仓库id
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "仓库")
    private String warehouseName;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 物品名称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "物品名称")
    private String itemName;

    /**
     * 物品编号
     */
    @ColumnWidth(18)
    @ExcelProperty(value = "物品编号")
    private String itemNo;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * sku名称
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "规格名称")
    private String skuName;

    /**
     * sku编号
     */
    @ColumnWidth(18)
    @ExcelProperty(value = "规格编号")
    private String skuNo;

    /**
     * 库存
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "库存数量")
    private Long quantity;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 单价
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

    /**
     * 总价
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "总价($)")
    private BigDecimal totalPrice;

    /**
     * 货架
     */
    @ColumnWidth(12)
    @ExcelProperty(value = "货架")
    private String storageShelf;

    /**
     * 备注
     */
    private String remark;


}
