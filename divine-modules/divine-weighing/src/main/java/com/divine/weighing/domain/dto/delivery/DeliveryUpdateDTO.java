package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 送货记录相关入参
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="送货记录相关入参")
public class DeliveryUpdateDTO implements Serializable{


	@Schema(description="id")
	private String id;

	@Schema(description="工厂磅单编号")
	private String companyWeightNo;

	@Schema(description="送货状态文本展示")
	private String statusText;

	@Schema(description="防伪码")
	private String antiFakeCode;

	@Schema(description="组货结算运费")
	private String settlePrice;

	@Schema(description="组货结算时间")
	private Date settleTime;

	@Schema(description="开票日期")
	private Date ticketTime;

	@Schema(description="授权日期")
	private Date accreditTime;
}
