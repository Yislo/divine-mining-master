package com.divine.weighing.domain.dto.export;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class DeliveryExportDTO implements Serializable{
	@Schema(description="矿主名称")
	private String ownerName;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="操作人")
	private String operator;

	@Schema(description="送货状态(0:已发车,1:已过磅,2:已完成)")
	private Integer status;

	@Schema(description="需要导出的表头(通过获取下拉框接口获取,类型:deliveryExportField)")
	private List<String> fields;

	@Schema(description="开始时间")
	private Date startTime;

	@Schema(description="结束时间")
	private Date endTime;
}
