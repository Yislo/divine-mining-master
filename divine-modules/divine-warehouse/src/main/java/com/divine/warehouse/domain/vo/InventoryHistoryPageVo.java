package com.divine.warehouse.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存记录
 *
 * @author yisl
 * @date 2024-07-22
 */
@Data
public class InventoryHistoryPageVo{
    /**
     * 业务单号
     */
    private String bizNO;
    /**
     * 业务id
     */
    private Long bizId;
    /**
     * 业务类型
     */
    private Integer bizType;
    /**
     * 仓库id
     */
    private Long warehouseId;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 操作后数量
     */
    private Long afterQuantity;
    /**
     * 操作前数量
     */
    private Long beforeQuantity;
    /**
     * 数量
     */
    private Long quantity;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 物品比那好
     */
    private String itemNo;
    /**
     * 物品名称
     */
    private String itemName;
    /**
     * 规格id
     */
    private Long skuId;
    /**
     * 规格编号
     */
    private String skuNo;
    /**
     * 规格名称
     */
    private String skuName;

}
