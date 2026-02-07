package com.divine.weighing.domain.vo.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 发车记录相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "送货数量")
public class DeliveryNumVO implements Serializable {

    @Schema(description = "全部送货数")
    private Long allNum;

    @Schema(description = "已发车数量")
    private Long departureNum;

    @Schema(description = "已过磅数量")
    private Long weightNum;

    @Schema(description = "已完成数量")
    private Long completeNum;
}
