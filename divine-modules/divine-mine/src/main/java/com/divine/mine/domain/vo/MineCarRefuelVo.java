package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.mine.domain.entity.MineCarRefuel;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 车辆加油记录视图对象 mine_car_refuel
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineCarRefuel.class)
public class MineCarRefuelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 加油单编号
     */
    private String refuelNo;

    /**
     * 加油单状态
     */
    private Integer refuelStatus;

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
     * 负责人
     */
    private String createBy;

    /**
     * 加油时间
     */
    private LocalDateTime createTime;


}
