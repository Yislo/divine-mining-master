package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单业务对象 wms_receipt_order
 *
 * @author zcc
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ReceiptOrder.class, reverseConvertGenerate = false)
public class ReceiptOrderDto extends BaseOrderDto<ReceiptOrderDetailDto> {

    /**
     * 入库类型
     */
    @Schema(description = "入库类型")
    @NotNull(message = "入库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long optType;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String bizOrderNo;

    /**
     * 对接商户
     */
    @Schema(description = "对接商户")
    private Long merchantId;

    /**
     * 仓库id
     */
    @Schema(description = "仓库id")
    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

}
