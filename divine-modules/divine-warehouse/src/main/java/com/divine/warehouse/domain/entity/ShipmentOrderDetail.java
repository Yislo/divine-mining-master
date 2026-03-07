package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单详情对象 wms_shipment_order_detail
 *
 * @author yisl
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment_order_detail")
public class ShipmentOrderDetail extends BaseOrderDetail {
    /**
     * 出库单id
     */
    private Long shipmentId;
    /**
     * 所属仓库
     */
    private Long warehouseId;
}
