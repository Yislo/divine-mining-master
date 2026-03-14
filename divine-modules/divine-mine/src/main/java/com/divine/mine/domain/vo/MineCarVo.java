package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineCar;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;

/**
 * 车辆信息视图对象 mine_car
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineCar.class)
public class MineCarVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

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
     * 驾驶员电话
     */
    @ExcelProperty(value = "驾驶员电话")
    private String driverPhone;

    /**
     * 驾驶员
     */
    @ExcelProperty(value = "驾驶员")
    private String driver;

    /**
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private Long carType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 是否删除(0:未删除,1:已删除)
     */
    @ExcelProperty(value = "是否删除(0:未删除,1:已删除)")
    private Integer isDel;


}
