package com.divine.mine.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 回磅 DTO
 *
 * @author yisl
 * @date 2026-03-13
 */
@Data
public class WeightingReturnDto {

    /** 主键id */
    @NotNull(message = "主键id不为空")
    private Long id;

    /** 总重 */
    private Double totalWeight;

    /** 皮重 */
    private Double tareWeight;

    /** 净重(总重-皮重) */
    private Double netWeight;

}
