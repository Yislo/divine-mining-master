package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
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
public class MoveDetailVO {

    /**
     * 移库单id
     */
    private Long moveId;

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
     * 源货架
     */
    private String sourceStorageShelf;

    /**
     * 目标货架
     */
    private String targetStorageShelf;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;



}
