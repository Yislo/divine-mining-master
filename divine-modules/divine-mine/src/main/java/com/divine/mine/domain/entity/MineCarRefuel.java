package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serial;

/**
 * 车辆加油记录对象 mine_car_refuel
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mine_car_refuel")
public class MineCarRefuel extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 加油单编号
     */
    private String refuelNo;
    /**
     * 车辆id
     */
    private Long carId;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 车辆编号
     */
    private String carNo;
    /**
     * 是否外来车辆(0:否,1:是)
     */
    private Long isExternal;
    /**
     * 加油时里程表(km)
     */
    private Long odometer;
    /**
     * 加油量(L)
     */
    private Long litre;
    /**
     * 加油类型(0:柴油,1:汽油)
     */
    private String refuelType;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否删除(0:未删除,1:已删除)
     */
    private Long isDel;

}
