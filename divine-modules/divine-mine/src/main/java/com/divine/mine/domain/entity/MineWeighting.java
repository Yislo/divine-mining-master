package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.io.Serial;

/**
 * 过磅记录对象 mine_weighting
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mine_weighting")
public class MineWeighting extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /** 车牌号 */
    private String carNumber;

    /** 过磅单编号 */
    private String weighingNo;

    /** 商品类型 */
    private Integer goodsType;

    /** 物资名称 */
    private String goodsName;

    /** 发货单位 */
    private Long shipMerchantId;

    /** 收货单位 */
    private Long receiptMerchantId;

    /** 发货日期 */
    private LocalDateTime shipTime;

    /** 发货地址 */
    private String shipAddress;

    /** 总重 */
    private Double totalWeight;

    /** 皮重 */
    private Double tareWeight;

    /** 净重 */
    private Double netWeight;

    /** 过磅员 */
    private String weighingOperator;

    /** 过磅时间 */
    private LocalDateTime weighingTime;

    /** 回磅员 */
    private String returnOperator;

    /** 回磅时间 */
    private LocalDateTime returnTime;

    /** 收货日期 */
    private LocalDateTime deliveryTime;

    /** 过磅类型(0=发货,1=收货) */
    private Integer weighingType;

    /** 过磅状态(0:已过磅,1:已回磅,-1:作废) */
    private Integer weighingStatus;

    /** 质量单id */
    private Long qualityId;

    /** 备注 */
    private String remark;

}
