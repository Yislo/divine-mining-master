package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.common.excel.annotation.ExcelDictFormat;
import com.divine.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆加油记录视图对象 mine_car_refuel
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class CarRefuelExcelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ColumnWidth(16)
    @ExcelProperty(value = "加油单编号")
    private String refuelNo;

    @ColumnWidth(12)
    @ExcelProperty(value = "车牌号")
    private String carNumber;

    @ColumnWidth(12)
    @ExcelProperty(value = "车辆编号")
    private String carNo;

    @ColumnWidth(12)
    @ExcelProperty(value = "车辆类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "car_type")
    private Integer carType;

    @ColumnWidth(12)
    @ExcelProperty(value = "外来车辆", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "is_external")
    private Long isExternal;

    @ColumnWidth(14)
    @ExcelProperty(value = "加油量(L)")
    private Long litre;

    @ColumnWidth(24)
    @ExcelProperty(value = "加油时里程表(km)")
    private Long odometer;

    @ColumnWidth(12)
    @ExcelProperty(value = "加油类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "refuel_type")
    private String refuelType;

    @ColumnWidth(12)
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "refuel_status")
    private Integer refuelStatus;

    @ColumnWidth(12)
    @ExcelProperty(value = "备注")
    private String remark;

    @ColumnWidth(20)
    @ExcelProperty(value = "加油时间")
    private LocalDateTime createTime;

    @ColumnWidth(12)
    @ExcelProperty(value = "负责人")
    private String createBy;


}
