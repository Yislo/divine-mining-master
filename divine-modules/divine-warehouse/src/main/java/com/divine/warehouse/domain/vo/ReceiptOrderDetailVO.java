package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = ReceiptOrderDetail.class)
public class ReceiptOrderDetailVO extends BaseOrderDetailVO {

    /**
     * 入库单id
     */
    private Long receiptId;

}
