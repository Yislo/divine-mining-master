package com.divine.mine.domain.dto;

import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.mine.domain.entity.MineWeighting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDateTime;

/**
 * 过磅编辑 DTO
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MineWeighting.class, reverseConvertGenerate = false)
public class MineWeightingDto extends BaseEntity {

    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    private String weighingNo;

    private String carNumber;

    private Integer goodsType;

    private String goodsName;

    private Integer weighingType;

    private Integer weighingStatus;

    private Long shipMerchantId;

    private Long receiptMerchantId;

    private LocalDateTime shipTime;

    private String shipAddress;

    private LocalDateTime deliveryTime;

    private Double totalWeight;

    private Double tareWeight;

    private Double netWeight;

    private String remark;

}
