package com.divine.weighing.domain.vo.refuel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 加油记录相关出参
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="加油记录相关出参")
public class RefuelPageVO implements Serializable{

	@Schema(description="id")
	private String id;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="所属车队")
	private String motorcadeName;

	@Schema(description="所属车队")
	private String motorcadeId;

	@Schema(description="加油量(L)")
	private Integer litre;

	@Schema(description="是否结算(0:未结算,1:已结算)")
	private Integer status;

	@Schema(description="是否结算")
	private String statusText;

	@Schema(description="相关图片(凭据)")
	private String imageUrl;

	@Schema(description="操作人")
	private String createByName;

	@Schema(description="备注")
	private String remark;

	@Schema(description="加油时间")
	private Date createTime;


}
