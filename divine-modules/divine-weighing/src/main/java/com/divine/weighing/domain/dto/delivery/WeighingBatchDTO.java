package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 过磅记录相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="过磅记录相关入参")
public class WeighingBatchDTO implements Serializable{

    @Schema(description="是否新批次(0:否,1:是)")
    @NotNull(message = "是否新批次不能为空")
	private Integer isNewBatch;

    @Schema(description="过磅车辆")
    @NotEmpty(message = "过磅车辆不能为空")
    private List<WeighingDTO> weighingList;

}
