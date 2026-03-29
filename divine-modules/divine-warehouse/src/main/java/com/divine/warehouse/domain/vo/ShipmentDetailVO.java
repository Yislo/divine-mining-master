package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
public class ShipmentDetailVO {

    /**
     * 入库单id
     */
    private Long shipmentId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品编号
     */
    private String itemNo;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 规格编号
     */
    private String skuNo;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;

    /**
     * 总价($)
     */
    private BigDecimal totalPrice;

    /**
     * 货架
     */
    private String storageShelf;



}
