package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrderDetail.class)
public class ReceiptInfoVO {

    /**
     * 明细id
     */
    private Long id;

    /**
     * 入库单id
     */
    private Long receiptId;

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
     * 货架
     */
    private String storageShelf;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;

    /**
     * 总价($)
     */
    private BigDecimal totalPrice;


    /**
     * 总价($)
     */
    private List<String> fileList;



}
