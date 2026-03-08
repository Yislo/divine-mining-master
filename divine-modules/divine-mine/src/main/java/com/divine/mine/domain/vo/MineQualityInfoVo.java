package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineQuality;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
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
     * 过磅编号
     */
    private String weightingNo;

    /**
     * 送货单位id
     */
    private Long shipMerchantId;

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
     * 样品明细
     */
    private List<MineWeightingVo> WeightingList;


}
