package com.divine.warehouse.domain.dto;

import com.divine.common.mybatis.core.page.BasePage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 出库单业务对象 wms_shipment_order
 *
 * @author yisl
 * @date 2024-08-01
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ShipmentInfoDto extends BasePage {

    /**
     * 出库单id
     */
    @NotNull(message = "出库单id不能为空")
    private Long shipmentId;

    /**
     * 物品(物品名称和物品编号通用搜索字段)
     */
    private String item;

    /**
     * 规格(规格名称和规格编号通用搜索字段)
     */
    private String sku;


}
