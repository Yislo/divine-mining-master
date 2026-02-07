package com.divine.weighing.domain.vo.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 发车记录相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="发车记录相关出参")
public class DeliveryBatchPageVO implements Serializable{

	@Schema(description="批次号")
	private String batchCode;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="目的地工厂")
	private String companyName;

	@Schema(description="矿主名称")
	private String ownerName;

	@Schema(description="车数")
	private Integer carNum;

	@Schema(description="编号")
	private String carCode;

	@Schema(description="操作人")
	private String createByName;

	@Schema(description="过磅时间")
	private Date weighingTime;

	@Schema(description="当前状态(0:待回磅,1:已完成)")
	private Integer status;

	@Schema(description="当前状态(0:待回磅,1:已完成)")
	private String statusText;
//
//	@Schema(description="备注")
//	private List<String> remarkList;
}
