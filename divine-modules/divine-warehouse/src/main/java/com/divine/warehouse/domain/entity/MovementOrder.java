package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 移库单对象 wms_movement_order
 *
 * @author yisl
 * @date 2024-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_movement_order")
public class MovementOrder extends BaseOrder {

    /**
     * 移库单编号
     */
    private String moveNo;

    /**
     * 移库单状态
     */
    private Integer moveStatus;

    /**
     * 源仓库
     */
    private Long sourceWarehouseId;

    /**
     * 目标仓库
     */
    private Long targetWarehouseId;

}
