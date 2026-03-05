package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单业务对象 wms_shipment_order
 *
 * @author yisl
 * @date 2024-08-01
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ShipmentOrder.class, reverseConvertGenerate = false)
public class ShipmentOrderDto extends BaseOrderDto<ShipmentOrderDetailDto> {

    /**
     * 入库类型
     */
    @NotNull(message = "出库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long optType;

    /**
     * 领用人
     */
    private String recipient;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;
}
