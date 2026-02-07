package com.divine.weighing.domain.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 数据总计出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsVO implements Serializable{

	@Schema(description="待收款")
	private BigDecimal delayProceeds;

	@Schema(description="待收款账单数量")
	private Integer delayProceedsNum;

	@Schema(description="累计收款")
	private BigDecimal totalProceeds;

	@Schema(description="累计收款账单数量")
	private Integer totalProceedsNum;

	@Schema(description="累计送货量")
	private Integer deliverGoodsNum;

	@Schema(description="工厂数量")
	private Integer companyNum;

}
