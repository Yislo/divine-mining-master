package com.divine.weighing.domain.dto.deliveryquality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 送货质量相关入参
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="送货质量相关入参")
public class QualityUpdateDTO implements Serializable{

	@Schema(description="delivery表id")
	private String id;

    @Schema(description="总重(kg)")
	private Integer totalWeight;

    @Schema(description="净重(kg)")
	private Integer netWeight;

    @Schema(description="空重(kg)")
	private Integer emptyWeight;

    @Schema(description="水份")
	private BigDecimal moisture;

    @Schema(description="氧化铜品味")
	private BigDecimal cuoRatio;

    @Schema(description="酸耗")
	private BigDecimal acidDemand;
}
