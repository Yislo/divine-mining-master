package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class DepartureSaveDTO implements Serializable{

    @Schema(description="矿主id")
    @NotBlank(message = "矿主不能为空")
	private String ownerId;

    @Schema(description="车牌号")
    @NotEmpty(message = "车牌号不能为空")
	private List<DepartureDTO> carList;

//    @Schema(description="编号")
//    @NotBlank(message = "编号不能为空")
//	private String carCode;

    @Schema(description="目的地工厂id")
    @NotBlank(message = "目的地不能为空")
	private String companyId;

    @Schema(description="发车时间")
    @NotNull(message = "发车时间不能为空")
	private Date departureTime;
}
