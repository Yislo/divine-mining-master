package com.divine.mine.domain.vo;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.mine.domain.entity.MineWeighting;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;

/**
 * 过磅记录 VO
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = MineWeighting.class)
public class MineWeightingVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "车牌号")
    private String carNumber;

    @ExcelProperty(value = "过磅单编号")
    private String weighingNo;

    @ExcelProperty(value = "商品类型")
    private Integer goodsType;

    @ExcelProperty(value = "物资名称")
    private String goodsName;

    @ExcelProperty(value = "发货单位")
    private Long shipMerchantId;

    @ExcelProperty(value = "发货单位")
    private String shipMerchantName;

    @ExcelProperty(value = "收货单位")
    private Long receiptMerchantId;

    @ExcelProperty(value = "收货单位名称")
    private String receiptMerchantName;

    @ExcelProperty(value = "发货日期")
    private LocalDateTime shipTime;

    @ExcelProperty(value = "发货地址")
    private String shipAddress;

    @ExcelProperty(value = "收货日期")
    private LocalDateTime deliveryTime;

    @ExcelProperty(value = "过磅状态")
    private Integer weighingStatus;

    @ExcelProperty(value = "过磅类型")
    private Integer weighingType;

    @ExcelProperty(value = "总重(kg)")
    private Double totalWeight;

    @ExcelProperty(value = "皮重(kg)")
    private Double tareWeight;

    @ExcelProperty(value = "净重(kg)")
    private Double netWeight;

    @ExcelProperty(value = "质量单id")
    private Long qualityId;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "创建人")
    private String createBy;

    @ExcelProperty(value = "过磅员")
    private String weighingOperator;

    @ExcelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelProperty(value = "过磅时间")
    private LocalDateTime weighingTime;

    @ExcelProperty(value = "回磅员")
    private String returnOperator;

    @ExcelProperty(value = "回磅时间")
    private LocalDateTime returnTime;

}
