package com.divine.weighing.domain.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 14:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartogramInfoVO implements Serializable{

    @Schema(description="工厂id")
    private String companyId;

    @Schema(description="工厂名称")
    private String companyName;

    @Schema(description="已发车")
    private Integer departureNum;

    @Schema(description="已过磅")
    private Integer weighingNum;

    @Schema(description="未到")
    private Integer totalNum;

}
