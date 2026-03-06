package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.Inventory;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存业务对象 wms_inventory
 *
 * @author yisl
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Inventory.class, reverseConvertGenerate = false)
public class InventoryDto extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规格ID
     */
    @NotNull(message = "规格ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 所属仓库
     */
    @NotNull(message = "所属仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    /**
     * 库存
     */
    @NotNull(message = "库存不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long quantity;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最大数量
     */
    private Long maxQuantity;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品编号
     */
    private String itemNo;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 规格编号
     */
    private String skuNo;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 类别
     */
    private Long itemCategory;
}
