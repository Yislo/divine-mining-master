package com.divine.weighing.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionVO {

    @Schema(description = "展示值")
    private String label;
    @Schema(description = "实际值")
    private String value;
}
