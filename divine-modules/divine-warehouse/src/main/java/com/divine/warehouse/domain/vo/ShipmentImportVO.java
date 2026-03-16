
package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.common.excel.annotation.ExcelDictFormat;
import com.divine.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库单业务对象 wms_shipment_order
 *
 * @author yisl
 * @date 2024-08-01
 */

@Data
public class ShipmentImportVO {

    @ColumnWidth(16)
    @ExcelProperty(value = "出库单号")
    private String shipmentNo;

    @ColumnWidth(18)
    @ExcelProperty(value = "物品名称")
    private String itemName;

    @ColumnWidth(18)
    @ExcelProperty(value = "规格")
    private String skuName;

    @ExcelProperty(value = "数量")
    private Long quantity;

    @ColumnWidth(15)
    @ExcelProperty(value = "单价($)")
    private BigDecimal unitPrice;

    @ColumnWidth(15)
    @ExcelProperty(value = "总价($)")
    private BigDecimal totalPrice;

    @ColumnWidth(20)
    @ExcelProperty(value = "出库时间")
    private Date shipmentTime;

    @ColumnWidth(18)
    @ExcelProperty(value = "所属仓库")
    private String warehouseName;

    @ColumnWidth(15)
    @ExcelProperty(value = "出库状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "wms_shipment_status")
    private String shipmentStatus;

    @ColumnWidth(15)
    @ExcelProperty(value = "领用人")
    private String recipient;

    @ColumnWidth(25)
    @ExcelProperty(value = "备注")
    private String remark;

}
