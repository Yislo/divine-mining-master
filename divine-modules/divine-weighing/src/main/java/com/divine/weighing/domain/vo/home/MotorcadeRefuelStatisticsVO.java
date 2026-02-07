package com.divine.weighing.domain.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 14:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MotorcadeRefuelStatisticsVO {

    @Schema(description="车队名称")
    private String motorcadeName;

    @Schema(description="加油量(L)")
    private Integer refuelNum;

}
