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
 * @Date: 2025/11/20 21:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PieChartVO implements Serializable {

    @Schema(description="工厂名称")
    private String companyName;

    @Schema(description="发车未过磅数量")
    private Integer departureNum;

    @Schema(description="发车已过磅数量")
    private Integer weighingNum;

    @Schema(description="总数")
    private Integer totalNum;
}
