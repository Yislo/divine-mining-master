package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.Inventory;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存业务对象 wms_inventory
 *
 * @author zcc
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Inventory.class, reverseConvertGenerate = false)
public class InventoryDto extends BaseEntity {

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规格ID
     */
    @Schema(description = "规格ID")
    @NotNull(message = "规格ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 所属仓库
     */
    @Schema(description = "所属仓库")
    @NotNull(message = "所属仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    /**
     * 库存
     */
    @Schema(description = "库存")
    @NotNull(message = "库存不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quantity;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 最小数量
     */
    @Schema(description = "最小数量")
    private BigDecimal minQuantity;


    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品编号")
    private String itemCode;

    @Schema(description = "规格名称")
    private String skuName;

    @Schema(description = "规格编号")
    private String skuCode;

    @Schema(description = "物品id")
    private Long itemId;

    @Schema(description = "itemCategory")
    private Long itemCategory;
}
