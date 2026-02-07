package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseOrderDto<T extends BaseOrderDetailDto> extends BaseEntity {
    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 业务单号
     */
    @Schema(description = "业务单号")
    @NotBlank(message = "入库单号单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String orderNo;

    /**
     * 商品总数
     */
    @Schema(description = "商品总数")
    private BigDecimal totalQuantity;

    /**
     * 订单总金额
     */
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态")
    private Integer orderStatus;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 商品信息
     */
    @Schema(description = "商品信息")
    private List<T> details;
}
