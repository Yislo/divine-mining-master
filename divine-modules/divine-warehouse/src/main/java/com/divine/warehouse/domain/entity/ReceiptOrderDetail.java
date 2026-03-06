package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 入库单详情对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("wms_receipt_order_detail")
public class ReceiptOrderDetail extends BaseOrderDetail {

    /**
     * 所属入库单id
     */
    private Long receiptId;

    /**
     * 所属仓库
     */
    private Long warehouseId;


}
