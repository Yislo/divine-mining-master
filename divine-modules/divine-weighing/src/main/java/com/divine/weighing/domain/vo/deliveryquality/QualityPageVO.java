package com.divine.weighing.domain.vo.deliveryquality;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

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
public class QualityPageVO implements Serializable{

	@Schema(description="delivery表id")
	private String deliveryId;

	@Schema(description="过磅编号")
	private String weightNo;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="矿主")
	private String ownerName;

	@Schema(description="目的地工厂")
	private String companyName;

	@Schema(description="总重(kg)")
	private Integer totalWeight;

	@Schema(description="净重(kg)")
	private Integer netWeight;

	@Schema(description="空重(kg)")
	private Integer emptyWeight;

	@Schema(description="水份")
	private String moisture;

	@Schema(description="氧化铜品味")
	private String cuoRatio;

	@Schema(description="酸耗")
	private String acidDemand;

	@Schema(description="金吨-仅管理员可见")
	private String goldTon;

	@Schema(description = "过磅时间")
	private Date weighingTime;
}
