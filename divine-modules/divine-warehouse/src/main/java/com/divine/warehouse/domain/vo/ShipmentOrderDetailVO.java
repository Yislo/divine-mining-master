package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单详情视图对象 wms_shipment_order_detail
 *
 * @author yisl
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = ShipmentOrderDetail.class)
public class ShipmentOrderDetailVO extends BaseOrderDetailVO {
    /**
     * 出库单id
     */
    @ExcelProperty(value = "出库单id")
    private Long shipmentId;

}
