package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 过磅单编号
     */
    private String weighingNo;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 发货单位
     */
    private Long shipMerchantId;
    /**
     * 发货日期
     */
    private Date shipTime;
    /**
     * 发货地址
     */
    private String shipAddress;
    /**
     * 收货单位
     */
    private String deliveryMerchant;
    /**
     * 收货日期
     */
    private Date deliveryTime;
    /**
     * 过磅状态(0:已过磅,1:已回磅)
     */
    private Integer weighingStatus;
    /**
     * 总重
     */
    private Long totalWeight;
    /**
     * 皮重
     */
    private Long tareWeight;
    /**
     * 净重
     */
    private Long netWeight;
    /**
     * 备注
     */
    private String remark;
    /**
     * 质量id(取样之后生成)
     */
    private Long qualityId;

}
