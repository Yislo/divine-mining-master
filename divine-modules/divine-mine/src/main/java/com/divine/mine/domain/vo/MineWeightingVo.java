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
 * 过磅记录视图对象 mine_weighting
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

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String carNumber;

    /**
     * 过磅单编号
     */
    @ExcelProperty(value = "过磅单编号")
    private String weighingNo;

    /**
     * 商品类型
     */
    @ExcelProperty(value = "商品类型")
    private Integer goodsType;

    /**
     * 发货单位
     */
    @ExcelProperty(value = "发货单位")
    private Long shipMerchantId;

    /**
     * 发货单位
     */
    @ExcelProperty(value = "发货单位")
    private String shipMerchantName;

    /**
     * 发货日期
     */
    @ExcelProperty(value = "发货日期")
    private LocalDateTime shipTime;

    /**
     * 发货地址
     */
    @ExcelProperty(value = "发货地址")
    private String shipAddress;

    /**
     * 收货单位
     */
    @ExcelProperty(value = "收货单位")
    private String deliveryMerchant;

    /**
     * 收货日期
     */
    @ExcelProperty(value = "收货日期")
    private LocalDateTime deliveryTime;

    /**
     * 过磅状态
     */
    @ExcelProperty(value = "过磅状态")
    private Integer weighingStatus;

    /**
     * 总重
     */
    @ExcelProperty(value = "总重")
    private Long totalWeight;

    /**
     * 皮重
     */
    @ExcelProperty(value = "皮重")
    private Long tareWeight;

    /**
     * 净重
     */
    @ExcelProperty(value = "净重")
    private Long netWeight;

    /**
     * 质量id(取样之后生成)
     */
    private Long qualityId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 过磅时间
     */
    @ExcelProperty(value = "过磅时间")
    private LocalDateTime createTime;

    /**
     * 回磅时间
     */
    @ExcelProperty(value = "回磅时间")
    private LocalDateTime returnTime;


}
