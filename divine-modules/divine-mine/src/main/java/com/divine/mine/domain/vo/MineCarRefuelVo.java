package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineCarRefuel;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;
import java.io.Serial;
import java.util.Date;

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
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 加油单编号
     */
    @ExcelProperty(value = "加油单编号")
    private String refuelNo;

    /**
     * 车辆id
     */
    @ExcelProperty(value = "车辆id")
    private Long carId;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String carNumber;

    /**
     * 车辆编号
     */
    @ExcelProperty(value = "车辆编号")
    private String carNo;

    /**
     * 是否外来车辆(0:否,1:是)
     */
    @ExcelProperty(value = "是否外来车辆(0:否,1:是)")
    private Long isExternal;

    /**
     * 加油时里程表(km)
     */
    @ExcelProperty(value = "加油时里程表(km)")
    private Long odometer;

    /**
     * 加油量(L)
     */
    @ExcelProperty(value = "加油量(L)")
    private Long litre;

    /**
     * 加油类型(0:柴油,1:汽油)
     */
    @ExcelProperty(value = "加油类型(0:柴油,1:汽油)")
    private String refuelType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 加油时间
     */
    @ExcelProperty(value = "加油时间")
    private Date createTime;


}
