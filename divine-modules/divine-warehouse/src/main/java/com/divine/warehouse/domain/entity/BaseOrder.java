package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseOrder extends BaseEntity {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 物品总数
     */
    private Long totalQuantity;

    /**
     * 订单总金额
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;

}
