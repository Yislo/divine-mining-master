package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单视图对象 wms_receipt_order
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrder.class)
public class ReceiptOrderVo extends BaseOrderVo<ReceiptOrderDetailVO> {

    /**
     * 入库单号
     */
    private String receiptNo;

    /**
     * 入库状态(0:未入库,1:已入库,-1:已作废)
     */
    private Integer receiptStatus;

    /**
     * 入库类型
     */
    @ExcelProperty(value = "入库类型")
    private Long optType;

    /**
     * 供应商
     */
    @ExcelProperty(value = "对接商家id")
    private Long merchantId;

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String merchantName;

    /**
     * 第三方订单号
     */
    @ExcelProperty(value = "第三方订单号")
    private String bizOrderNo;
}
