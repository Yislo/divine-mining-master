package com.divine.weighing.domain.vo.deliveryquality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 送货质量相关出参
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="送货质量相关出参")
public class QualityStatisticsVO implements Serializable{

	@Schema(description="矿主Id")
	private String ownerId;

	@Schema(description="矿主名称")
	private String ownerName;

	@Schema(description="工厂id")
	private String companyId;

	@Schema(description="工厂名称")
	private String companyName;

	@Schema(description="净重(kg)")
	private Integer netWeight;

	@Schema(description="总重(kg)")
	private Integer totalWeight;

	@Schema(description="水份")
	private BigDecimal moisture;

	@Schema(description="氧化铜品味")
	private BigDecimal cuoRatio;
}
