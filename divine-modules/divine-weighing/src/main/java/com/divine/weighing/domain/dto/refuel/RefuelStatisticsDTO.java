package com.divine.weighing.domain.dto.refuel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/20 20:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefuelStatisticsDTO {

    @Schema(description = "开始时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;

    @Schema(description = "所属公司")
    private String companyId;


}
