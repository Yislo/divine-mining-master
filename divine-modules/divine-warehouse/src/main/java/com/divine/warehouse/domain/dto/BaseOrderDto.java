package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseOrderDto<T extends BaseOrderDetailDto> extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 业务单号
     */
    private String bizNo;

    /**
     * 总数
     */
    private BigDecimal totalQuantity;

    /**
     * 总金额
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 物品信息
     */
    @NotEmpty(message = "请先选择物品")
    private List<T> details;
}
