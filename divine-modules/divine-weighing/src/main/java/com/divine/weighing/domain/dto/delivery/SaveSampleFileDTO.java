package com.divine.weighing.domain.dto.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 取样文件相关入参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="取样文件相关入参")
public class SaveSampleFileDTO implements Serializable{

    @Schema(description="id")
    @NotBlank(message = "id不能为空")
    private String id;

    @Schema(description="文件地址")
    @NotEmpty(message = "文件地址不能为空")
	private List<String> fileList;

}
