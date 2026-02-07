package com.divine.weighing.domain.vo.delivery;

import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
@EqualsAndHashCode(callSuper = true)
@Schema(description="发车记录相关出参")
public class DepartureVO extends BaseEntity implements Serializable{

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="矿主名称")
	private String mineOwnerName;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="目的地工厂")
	private String companyName;

	@Schema(description="送货状态(0:未到站1:已过磅,2:已完成)")
	private Integer status;

	@Schema(description="发车时间")
	private Date departureTime;

	@Schema(description="备注")
	private String remark;
}
