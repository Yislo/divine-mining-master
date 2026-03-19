package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineQuality;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 送货质量视图对象 mine_quality
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineQuality.class)
public class MineQualityInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 质量编号
     */
    private String qualityNo;

    /**
     * 质量单状态
     */
    private Integer qualityStatus;

    /**
     * 水份
     */
    private BigDecimal moisture;

    /**
     * 氧化铜品位
     */
    private BigDecimal cuoRatio;

    /**
     * 酸耗
     */
    private BigDecimal acidDemand;

    /**
     * 取样时间
     */
    private Date createTime;

    /**
     * 取样时间
     */
    private String remark;

    /**
     * 样品明细
     */
    private List<MineWeightingVo> WeightingList;


}
