package com.divine.weighing.domain.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 14:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyStatisticsVO implements Serializable{

    @Schema(description="工厂名称")
    private String companyName;

    @Schema(description="送货数量")
    private Integer deliveryNum;

    @Schema(description = "平均品位-仅管理员可见")
    private String averageRatio;


}
