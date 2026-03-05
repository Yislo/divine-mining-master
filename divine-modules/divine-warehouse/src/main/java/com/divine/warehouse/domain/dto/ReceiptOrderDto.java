package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单业务对象 wms_receipt_order
 *
 * @author yisl
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ReceiptOrder.class, reverseConvertGenerate = false)
public class ReceiptOrderDto extends BaseOrderDto<ReceiptOrderDetailDto> {

    /**
     * 入库类型(0:采购入库,1:归还入库)
     */
    @NotNull(message = "入库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long optType;

    /**
     * 第三方订单号
     */
    private String bizOrderNo;

    /**
     * 对接商户
     */
    private Long merchantId;

    /**
     * 入库状态
     */
    private Integer receiptStatus;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

}
