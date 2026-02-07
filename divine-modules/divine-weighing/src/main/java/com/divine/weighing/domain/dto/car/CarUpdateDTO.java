package com.divine.weighing.domain.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 车辆信息相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="车辆信息相关入参")
public class CarUpdateDTO implements Serializable{

	@Schema(description="主键id")
	private String id;

    @Schema(description="车牌号")
	@NotBlank(message = "车牌号不能为空")
	private String carNumber;

    @Schema(description="所属车队id")
	@NotBlank(message = "请选择所属车队")
	private String motorcadeId;

    @Schema(description="备注")
	private String remark;

}
