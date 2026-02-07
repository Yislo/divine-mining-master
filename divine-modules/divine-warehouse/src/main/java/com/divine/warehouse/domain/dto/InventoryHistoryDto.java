package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseHistoryEntity;
import com.divine.warehouse.domain.entity.InventoryHistory;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存记录业务对象 wms_inventory_history
 *
 * @author zcc
 * @date 2024-07-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = InventoryHistory.class, reverseConvertGenerate = false)
public class InventoryHistoryDto extends BaseHistoryEntity {

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 操作id（出库、入库、库存移动表单id）
     */
    @Schema(description = "操作id（出库、入库、库存移动表单id）")
    @NotNull(message = "操作id（出库、入库、库存移动表单id）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderId;

    /**
     * 操作单号（入库、出库、移库、盘库单号）
     */
    @Schema(description = "操作单号（入库、出库、移库、盘库单号）")
    @NotNull(message = "操作单号（入库、出库、移库、盘库单号）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderNo;

    /**
     * 订单类型
     */
    @Schema(description = "订单类型")
    @NotNull(message = "订单类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer orderType;

    /**
     * 物料ID
     */
    @Schema(description = "物料ID")
    @NotNull(message = "物料ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 金额
     */
    @Schema(description = "金额")
    private BigDecimal amount;

    /**
     * 库存变化
     */
    @Schema(description = "库存变化")
    @NotNull(message = "库存变化不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quantity;

    /**
     * 备注
     */
    @Schema(description = "备注")
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 所属仓库
     */
    @Schema(description = "所属仓库")
    @NotNull(message = "所属仓库不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long warehouseId;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品编号")
    private String itemCode;

    @Schema(description = "规格")
    private String skuName;

    @Schema(description = "规格编号")
    private String skuCode;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;

}
