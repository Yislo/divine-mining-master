package com.divine.weighing.domain.vo.delivery;

import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 过磅记录相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="过磅记录相关出参")
public class WeighingVO extends BaseEntity implements Serializable{

	@Schema(description="矿主名称")
	private String mineOwnerName;

	@Schema(description="过磅批次号")
	private String weighingNum;

	@Schema(description="发车表id")
	private String departureId;

	@Schema(description="过磅时间")
	private Date weighingTime;

	@Schema(description="矿车编号")
	private String motorcadeCode;

	@Schema(description="签名人员")
	private String signName;

	@Schema(description="总重(kg)")
	private String totalWeight;

	@Schema(description="净重(kg)")
	private String netWeight;

	@Schema(description="空重(kg)")
	private String emptyWeight;

	@Schema(description="工厂id")
	private String companyId;

	@Schema(description="0-未删除 1-已删除")
	private Integer isDel;
}
