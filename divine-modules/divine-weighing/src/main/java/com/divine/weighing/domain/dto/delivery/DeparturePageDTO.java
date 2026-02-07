package com.divine.weighing.domain.dto.delivery;

import com.divine.common.mybatis.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 发车记录相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="发车记录相关入参")
public class DeparturePageDTO extends BasePage implements Serializable{

    @Schema(description="矿主id")
	private String ownerId;

    @Schema(description="车牌号")
	private String carNumber;

    @Schema(description="目的地工厂id")
	private String companyId;

    @Schema(description="操作人")
	private String operator;

    @Schema(description="送货状态(0:已发车,1:已过磅,2:已完成)")
	private Integer status;

    @Schema(description="开始时间")
    private Date startTime;

    @Schema(description="结束时间")
	private Date endTime;

//
//    @Schema(description="批次号")
//	private String batchCode;
//
//    @Schema(description="净重")
//	private Integer netWeight;
}
