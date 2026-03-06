package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=true)
public class BaseOrderDetail extends BaseEntity {
    /**
     * id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 数量
     */
    private Long quantity;
    /**
     * 单价($)
     */
    private BigDecimal unitPrice;
    /**
     * 货架
     */
    private String storageShelf;
    /**
     * 备注
     */
    private String remark;
}
