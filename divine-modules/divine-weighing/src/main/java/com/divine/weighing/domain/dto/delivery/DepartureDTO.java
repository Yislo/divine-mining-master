package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 发车记录相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="发车记录相关入参")
public class DepartureDTO implements Serializable{

    @Schema(description="车牌号")
    @NotBlank(message = "车牌号不能为空")
	private String carNumber;

    @Schema(description="备注")
	private String remark;
}
