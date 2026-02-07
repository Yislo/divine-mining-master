package com.divine.weighing.domain.dto.refuel;

import com.divine.common.mybatis.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 加油记录相关入参
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="加油记录相关入参")
public class RefuelPageDTO extends BasePage implements Serializable{

    @Schema(description="车牌号")
	private String carNumber;

    @Schema(description="所属车队id")
	private String motorcadeId;

    @Schema(description="是否结算(0:未结算,1:已结算)")
	private Integer status;

    @Schema(description="开始时间")
	private Date startTime;

    @Schema(description="结束时间")
	private Date endTime;
}
