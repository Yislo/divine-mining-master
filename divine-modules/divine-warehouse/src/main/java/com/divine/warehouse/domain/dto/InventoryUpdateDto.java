package com.divine.warehouse.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 库存业务对象 wms_inventory
 *
 * @author yisl
 * @date 2024-07-19
 */

@Data
public class InventoryUpdateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 货架
     */
    private String storageShelf;


    /**
     * 备注
     */
    private String remark;

}
