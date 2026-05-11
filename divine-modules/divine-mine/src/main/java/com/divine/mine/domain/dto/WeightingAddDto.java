package com.divine.mine.domain.dto;

import com.divine.mine.domain.entity.MineWeighting;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 过磅新增 DTO
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@AutoMapper(target = MineWeighting.class, reverseConvertGenerate = false)
public class WeightingAddDto {

    /** 车牌号 */
    @NotBlank(message = "车牌号不能为空")
    private String carNumber;

    /** 商品类型 */
    @NotNull(message = "商品类型不能为空")
    private Integer goodsType;

    /** 物资名称 */
    private String goodsName;

    /** 发货单位 */
    @NotNull(message = "发货单位不能为空")
    private Long shipMerchantId;

    /** 收货单位 */
    private Long receiptMerchantId;

    /** 发货日期 */
    private LocalDateTime shipTime;

    /** 发货地址 */
    private String shipAddress;

    /** 收货日期 */
    private LocalDateTime deliveryTime;

    /** 过磅类型(0=发货,1=收货) */
    @NotNull(message = "过磅类型不能为空")
    private Integer weighingType;

    /** 总重 */
    private Double totalWeight;

    /** 备注 */
    private String remark;

}
