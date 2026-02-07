package com.divine.weighing.domain.dto.car;

import com.divine.common.mybatis.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
@Schema(description="分页查询车辆信息入参")
public class CarPageDTO extends BasePage implements Serializable{

    @Schema(description="车牌号")
	private String carNumber;

    @Schema(description="所属车队id")
	private String motorcadeId;

}
