package com.divine.weighing.domain.dto.deliveryquality;

import com.divine.common.mybatis.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * @Description: 送货质量相关入参
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description="送货质量相关入参")
public class QualityPageDTO extends BasePage implements Serializable{

    @Schema(description="矿主id")
	private String ownerId;

    @Schema(description="目的地工厂id")
	private String companyId;

    @Schema(description="过磅编号(模糊查询)")
	private String weightNo;

    @Schema(description="净重")
	private Integer netWeight;

}
