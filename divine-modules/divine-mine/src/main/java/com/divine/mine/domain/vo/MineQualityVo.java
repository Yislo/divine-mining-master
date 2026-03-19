package com.divine.mine.domain.vo;

import java.math.BigDecimal;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.common.excel.annotation.ExcelDictFormat;
import com.divine.mine.domain.entity.MineQuality;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;
import java.util.Date;

/**
 * 送货质量视图对象 mine_quality
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineQuality.class)
public class MineQualityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 质量编号
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "质量单编号")
    private String qualityNo;

    /**
     * 质量单状态
     */
    @ExcelProperty(value = "质量单状态")
    @ExcelDictFormat(dictType = "quality_status")
    private Integer qualityStatus;

    /**
     * 送货单位id
     */
    private Long shipMerchantId;

    /**
     * 送货单位
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "送货单位")
    private String shipMerchantName;

    /**
     * 水份
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "水份(%)")
    private BigDecimal moisture;

    /**
     * 氧化铜品位
     */
    @ColumnWidth(20)
    @ExcelProperty(value = "氧化铜品位(%)")
    private BigDecimal cuoRatio;

    /**
     * 酸耗
     */
    @ExcelProperty(value = "酸耗(吨)")
    private BigDecimal acidDemand;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 取样时间
     */
    @ExcelProperty(value = "取样时间")
    private Date createTime;


}
