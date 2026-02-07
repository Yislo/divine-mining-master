package com.divine.weighing.domain.dto.home;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 14:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateDTO {

    @Schema(description="开始时间")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @Schema(description="结束时间")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;
}
