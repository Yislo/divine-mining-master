package com.divine.weighing.domain.vo.car;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/10/31 8:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页查询车辆信息出参")
public class CarPageVO {

    @Schema(description = "主键id")
    private String id;

    @Schema(description = "车牌号")
    private String carNumber;

    @Schema(description = "所属车队id")
    private String motorcadeId;

    @Schema(description = "所属车队")
    private String motorcadeName;

    @Schema(description="总送货数量")
    private Integer deliveryTotalNum;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private String createTime;

}
