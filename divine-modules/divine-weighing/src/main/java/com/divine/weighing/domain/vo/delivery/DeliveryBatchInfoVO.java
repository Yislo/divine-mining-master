package com.divine.weighing.domain.vo.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 发车记录相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="取样批次详情出参")
public class DeliveryBatchInfoVO implements Serializable{

	@Schema(description="批次号")
	private String batchCode;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="目的地工厂")
	private String companyName;

	@Schema(description = "送货记录")
	private List<DeliveryInfoVO> deliveryList;

}
