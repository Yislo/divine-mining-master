package com.divine.weighing.domain.vo.car;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 车辆信息相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="车辆信息相关出参")
public class CarVO implements Serializable{

	@Schema(description="主键id")
	private String id;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="所属车队id")
	private String motorcadeId;

}
