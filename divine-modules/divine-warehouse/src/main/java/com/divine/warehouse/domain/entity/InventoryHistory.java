package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.divine.common.mybatis.core.domain.BaseHistoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 库存记录对象 wms_inventory_history
 *
 * @author yisl
 * @date 2024-07-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wms_inventory_history")
public class InventoryHistory extends BaseHistoryEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 操作id（出库、入库、库存移动表单id）
     */
    private Long orderId;
    /**
     * 操作单号（入库、出库、移库、盘库单号）
     */
    private String orderNo;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 物料ID
     */
    private Long skuId;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 库存变化
     */
    private Long quantity;
    /**
     * 更新前数量
     */
    private Long beforeQuantity;
    /**
     * 更新后数量
     */
    private Long afterQuantity;
    /**
     * 备注
     */
    private String remark;
    /**
     * 所属仓库
     */
    private Long warehouseId;

}
