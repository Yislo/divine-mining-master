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
public class RefuelStatisticsVO {

    @Schema(description="今天加油车数")
    private Integer todayRefuelCar;

    @Schema(description="今日加油量(L)")
    private Integer todayRefuel;

    @Schema(description="本月加油车数")
    private Integer monthRefuelCar;

    @Schema(description="本月加油量(L)")
    private Integer monthRefuel;

}
