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
@AutoMapper(target = ReceiptOrderDetail.class)
public class    ReceiptDetailVO {

    /**
     * 入库单id
     */
    private Long receiptId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 货架
     */
    private String storageShelf;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;



}
