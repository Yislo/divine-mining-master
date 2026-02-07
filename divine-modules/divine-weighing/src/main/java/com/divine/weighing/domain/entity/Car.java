package com.divine.weighing.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: 车辆信息实体
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Car extends BaseEntity implements Serializable {

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
     * 所属车队id
     */
    private String motorcadeId;

    /**
     * 备注
     */
    private String remark;

}
