package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单视图对象 wms_shipment_order
 *
 * @author yisl
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = ShipmentOrder.class)
public class ShipmentOrderVo extends BaseOrderVo<ShipmentOrderDetailVO>{

    /**
     * 出库单号
     */
    private String shipmentNo;

    /**
     * 出库状态(0:未出库,1:已出库,-1:已作废)
     */
    private Integer shipmentStatus;

    /**
     * 出库类型
     */
    @ExcelProperty(value = "出库类型")
    private Integer optType;

    /**
     * 领用人
     */
    @ExcelProperty(value = "领用人")
    private String recipient;

    /**
     * 仓库名称
     */
    private String warehouseName;
}
