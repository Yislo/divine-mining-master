package com.divine.mine.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.divine.common.excel.annotation.ExcelDictFormat;
import com.divine.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 送货质量视图对象 mine_quality
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class QualityExcelVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 质量单id
     */
    private Long qualityId;

    @ColumnWidth(16)
    @ExcelProperty(value = "质量单编号")
    private String qualityNo;

    @ColumnWidth(12)
    @ExcelProperty(value = "车牌号")
    private String carNumber;

    @ColumnWidth(12)
    @ExcelProperty(value = "送货单位")
    private String shipMerchantName;

    @ColumnWidth(12)
    @ExcelProperty(value = "品位(%)")
    private BigDecimal cuoRatio;

    @ColumnWidth(12)
    @ExcelProperty(value = "水份(%)")
    private BigDecimal moisture;

    @ColumnWidth(12)
    @ExcelProperty(value = "酸耗(吨)")
    private BigDecimal acidDemand;

    @ColumnWidth(12)
    @ExcelProperty(value = "总重(kg)")
    private Long totalWeight;

    @ColumnWidth(12)
    @ExcelProperty(value = "皮重(kg)")
    private Long tareWeight;

    @ColumnWidth(12)
    @ExcelProperty(value = "净重(kg)")
    private Long netWeight;

    @ColumnWidth(12)
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "quality_status")
    private String qualityStatus;

    @ColumnWidth(12)
    @ExcelProperty(value = "商品类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "goods_type")
    private String goodsType;

    @ColumnWidth(20)
    @ExcelProperty(value = "取样时间")
    private LocalDateTime createTime;


}
