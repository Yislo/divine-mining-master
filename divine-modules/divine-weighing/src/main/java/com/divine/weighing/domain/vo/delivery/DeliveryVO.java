package com.divine.weighing.domain.vo.delivery;

import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 送货记录相关出参
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="送货记录相关出参")
public class DeliveryVO extends BaseEntity implements Serializable{

	@Schema(description="过磅批次号")
	private String weighingNum;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="矿主id")
	private String ownerId;

	@Schema(description="车辆编号")
	private String carCode;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="送货状态(0:已发车,1:已过磅,2:已完成)")
	private Integer status;

	@Schema(description="发车时间")
	private Date departureTime;

	@Schema(description="过磅时间")
	private Date weighingTime;

	@Schema(description="总重(kg)")
	private Integer totalWeight;

	@Schema(description="净重(kg)")
	private Integer netWeight;

	@Schema(description="空重(kg)")
	private Integer emptyWeight;

	@Schema(description="备注")
	private String remark;
}
