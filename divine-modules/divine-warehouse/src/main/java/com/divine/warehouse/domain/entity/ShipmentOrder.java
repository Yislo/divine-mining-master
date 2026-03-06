package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单对象 wms_shipment_order
 *
 * @author yisl
 * @date 2024-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_shipment_order")
public class ShipmentOrder extends BaseOrder {

    /**
     * 出库单编号
     */
    private String shipmentNo;

    /**
     * 出库单状态
     */
    private Integer shipmentStatus;

    /**
     * 仓库id
     */
    private Long warehouseId;

    /**
     * 入库类型
     */
    private Long optType;

    /**
     * 第三方订单号
     */
    private String bizOrderNo;

    /**
     * 领用人
     */
    private String recipient;

}
