package com.divine.weighing.domain.dto.refuel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 加油记录相关入参
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="加油记录相关入参")
public class RefuelSaveDTO implements Serializable{

    @Schema(description="车牌号")
	private String carNumber;

    @Schema(description="加油量(L)")
	private Integer litre;

    @Schema(description="备注")
    private String remark;

    @Schema(description="文件地址")
    private String filePath;
}
