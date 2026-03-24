package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.Inventory;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存视图对象 wms_inventory
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
public class InventoryExcelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "仓库名称")
    private String warehouseName;

    @ExcelProperty(value = "物品名称")
    private String itemName;

    @ExcelProperty(value = "物品编号")
    private String itemNo;

    @ExcelProperty(value = "规格名称")
    private String skuName;

    @ExcelProperty(value = "规格编号")
    private String skuNo;

    @ExcelProperty(value = "剩余库存")
    private String quantity;

    @ExcelProperty(value = "单位")
    private String unit;

    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

}
