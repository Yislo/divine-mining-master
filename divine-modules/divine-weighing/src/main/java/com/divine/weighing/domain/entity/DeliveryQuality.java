package com.divine.weighing.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 送货质量实体
 * @Author: yisl
 * @Date: 2025-12-13 18:24:08
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DeliveryQuality extends BaseEntity implements Serializable {

    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * delivery表id
     */
    private String deliveryId;

    /**
     * 矿主id
     */
    private String ownerId;

    /**
     * 目的地工厂id
     */
    private String companyId;

    /**
     * 总重(kg)
     */
    private Integer totalWeight;

    /**
     * 净重(kg)
     */
    private Integer netWeight;

    /**
     * 空重(kg)
     */
    private Integer emptyWeight;

    /**
     * 水份
     */
    private BigDecimal moisture;

    /**
     * 氧化铜品味
     */
    private BigDecimal cuoRatio;

    /**
     * 酸耗
     */
    private BigDecimal acidDemand;
}
