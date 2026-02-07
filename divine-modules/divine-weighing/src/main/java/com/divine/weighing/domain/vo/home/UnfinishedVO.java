package com.divine.weighing.domain.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 14:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnfinishedVO implements Serializable{

    @Schema(description="车辆id")
    private String id;

    @Schema(description="车牌号")
    private String carNumber;

    @Schema(description="目的地工厂")
    private String companyName;

    @Schema(description="所属车队")
    private String motorcadeName;

    @Schema(description="所属矿主")
    private String ownerName;

    @Schema(description="编号")
    private String codeNum;

    @Schema(description="发车时间")
    private Date departureTime;

    @Schema(description="备注")
    private String remark;


}
