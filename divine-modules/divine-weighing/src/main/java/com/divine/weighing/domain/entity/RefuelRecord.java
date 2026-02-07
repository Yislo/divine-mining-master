package com.divine.weighing.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: 加油记录实体
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class RefuelRecord extends BaseEntity implements Serializable {

    /**
     * 车牌号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 所属车队id
     */
    private String motorcadeId;

    /**
     * 加油量(L)
     */
    private Integer litre;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 是否结算(0:未结算,1:已结算)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
