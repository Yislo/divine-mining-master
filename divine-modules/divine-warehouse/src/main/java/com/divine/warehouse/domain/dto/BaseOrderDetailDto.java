package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseOrderDetailDto extends BaseEntity {
    /**
     * 入库单号
     */
    @Schema(description = "入库单号")
    @NotNull(message = "单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long orderId;
    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规格id
     */
    @Schema(description = "规格id")
    @NotNull(message = "规格id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long skuId;

    /**
     * 入库数量
     */
    @Schema(description = "入库数量")
    @NotNull(message = "出入库数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal quantity;

    /**
     * 金额
     */
    @Schema(description = "金额")
    private BigDecimal amount;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 所属仓库
     */
    @Schema(description = "所属仓库")
    private Long warehouseId;

    /**
     * 更新前数量
     */
    @Schema(description = "更新前数量")
    private BigDecimal beforeQuantity;
    /**
     * 更新后数量
     */
    @Schema(description = "更新后数量")
    private BigDecimal afterQuantity;
}
