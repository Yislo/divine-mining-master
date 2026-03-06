package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单对象 wms_receipt_order
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_receipt_order")
public class ReceiptOrder extends BaseOrder {

    /**
     * 入库单编号
     */
    private String receiptNo;

    /**
     * 入库单状态
     */
    private Integer receiptStatus;

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
     * 供应商
     */
    private Long merchantId;

}
