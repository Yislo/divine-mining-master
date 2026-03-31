package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

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
     * 出库单号
     */
    private String shipmentNo;

    /**
     * 出库类型
     */
    @NotNull(message = "出库类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long optType;

    /**
     * 领用人
     */
    private String recipient;

//    /**
//     * 物品(物品名称和物品编号通用搜索字段)
//     */
//    private String item;
//
//    /**
//     * 规格(规格名称和规格编号通用搜索字段)
//     */
//    private String sku;

    /**
     * 出库单id
     */
    private List<Long> shipmentIdList;

    /**
     * 仓库id
     */
    @NotNull(message = "仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    /**
     * 出库状态
     */
    private Integer shipmentStatus;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
