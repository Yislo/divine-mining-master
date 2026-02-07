package com.divine.weighing.domain.dto.car;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/10/31 8:13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="新增车辆信息相关入参")
public class CarSaveBatchDTO {

    @Schema(description="所属车队id")
    @NotBlank(message = "请选择所属车队")
    private String motorcadeId;

    @Schema(description="车牌号")
    @NotEmpty(message = "车牌号不能为空")
    private List<String> carNumber;
}
