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
 * @Description: 过磅记录相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="过磅记录相关入参")
public class WeighingDTO implements Serializable{

    @Schema(description="id")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description="总重")
    @NotNull(message = "总重不能为空")
    private Integer totalWeight;

    @Schema(description="备注")
	private String remark;

    @Schema(description="文件地址")
	private List<String> fileList;


}
