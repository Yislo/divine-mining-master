package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 回磅入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="回磅相关入参")
public class WeighingReturnDTO implements Serializable{

    @Schema(description = "回磅入参列表")
    @NotEmpty(message = "回磅参数不能为空")
    private List<WeighingWeightDTO> weighingWeightList;


}
