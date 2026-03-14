package com.divine.mine.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/13 18:08
 */
@Data
public class WeightingReturnDto {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不为空")
    private Long id;

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



}
