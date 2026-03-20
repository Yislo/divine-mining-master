package com.divine.mine.domain.vo;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineAccessRecord;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;

/**
 * 车辆出入厂记录视图对象 mine_access_record
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineAccessRecord.class)
public class MineAccessRecordVo implements Serializable {

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
     * 进厂时间
     */
    @ExcelProperty(value = "进厂时间")
    private LocalDateTime enterTime;

    /**
     * 出厂时间
     */
    @ExcelProperty(value = "出厂时间")
    private LocalDateTime exitTime;

    /**
     * 所属单位
     */
    @ExcelProperty(value = "所属单位")
    private Long merchantId;

    /**
     * 进厂类型(0:其他,1:送货,2:外来,3:空车)
     */
    @ExcelProperty(value = "进厂类型(0:其他,1:送货,2:外来,3:空车)")
    private Long entryType;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
