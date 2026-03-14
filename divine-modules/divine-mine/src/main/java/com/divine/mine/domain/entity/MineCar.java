package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serial;

/**
 * 车辆信息对象 mine_car
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mine_car")
public class MineCar extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 车辆编号
     */
    private String carNo;
    /**
     * 驾驶员电话
     */
    private String driverPhone;
    /**
     * 驾驶员
     */
    private String driver;
    /**
     * 车辆类型
     */
    private Long carType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否删除(0:未删除,1:已删除)
     */
    private Integer isDel;

}
