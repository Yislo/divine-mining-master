package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class WeighingWeightDTO implements Serializable{

    @Schema(description="id")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description="总重(kg)")
    @NotNull(message = "总重不能为空")
	private Integer totalWeight;

    @Schema(description="空重(kg)")
    @NotNull(message = "空重不能为空")
	private Integer emptyWeight;

    @Schema(description="净重(kg)")
    @NotNull(message = "净重不能为空")
	private Integer netWeight;

    @Schema(description="相关文件地址")
	private List<String> fileList;


}
