package com.divine.weighing.domain.vo.refuel;

import com.divine.common.mybatis.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: 加油记录相关出参
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="加油记录相关出参")
public class RefuelRecordVO extends BaseEntity implements Serializable{

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="加油量(L)")
	private Integer litre;
}
