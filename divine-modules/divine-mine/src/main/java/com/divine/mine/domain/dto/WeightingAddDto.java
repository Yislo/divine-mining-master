package com.divine.mine.domain.dto;

import com.divine.mine.domain.entity.MineWeighting;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 过磅记录业务对象 mine_weighting
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@AutoMapper(target = MineWeighting.class, reverseConvertGenerate = false)
public class WeightingAddDto {

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空")
    private String carNumber;

    /**
     * 商品类型
     */
    @NotBlank(message = "商品类型不能为空")
    private Integer goodsType;

    /**
     * 发货单位
     */
    @NotNull(message = "发货单位不能为空")
    private Long shipMerchantId;

    /**
     * 发货日期
     */
    private LocalDateTime shipTime;

    /**
     * 发货地址
     */
    private String shipAddress;

    /**
     * 收货单位
     */
    private String deliveryMerchant;

    /**
     * 收货日期
     */
    private LocalDateTime deliveryTime;

    /**
     * 总重
     */
    private Long totalWeight;

    /**
     * 皮重
     */
    private Long tareWeight;

    /**
     * 净重
     */
    private Long netWeight;

    /**
     * 备注
     */
    private String remark;


}
