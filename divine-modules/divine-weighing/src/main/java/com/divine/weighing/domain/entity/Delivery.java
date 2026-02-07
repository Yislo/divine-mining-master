package com.divine.weighing.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 送货记录实体
 * @Author: yisl
 * @Date: 2025-12-13 18:24:08
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Delivery extends BaseEntity implements Serializable {

    /**
     * id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 取样批次号
     */
    private String batchCode;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 工厂磅单编号
     */
    private String companyWeightNo;

    /**
     * 过磅编号
     */
    private String weightNo;

    /**
     * 矿主id
     */
    private String ownerId;

    /**
     * 目的地工厂id
     */
    private String companyId;

    /**
     * 所属车队id
     */
    private String motorcadeId;

    /**
     * 送货状态(0:已发车,1:已过磅,2:已完成)
     */
    private Integer status;

    /**
     * 防伪码
     */
    private String antiFakeCode;

    /**
     * 组货结算运费
     */
    private String settlePrice;

    /**
     * 组货结算日期
     */
    private Date settleTime;

    /**
     * 过磅时间
     */
    private Date weighingTime;

    /**
     * 发车时间
     */
    private Date departureTime;

    /**
     * 开票日期
     */
    private Date ticketTime;

    /**
     * 授权日期
     */
    private Date accreditTime;

    /**
     * 备注
     */
    private String remark;
}
