package com.divine.weighing.domain.vo.car;

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
 * @Date: 2025/11/3 11:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="查询车辆加油情况出参")
public class CarRefuelVO implements Serializable {

    @Schema(description="车牌号")
    private String carNumber;

    @Schema(description="距离上次加油送货次数")
    private Long weighingNum;

    @Schema(description="上次加油量")
    private Integer litre;

    @Schema(description="上次加油时间(如果返回空,就是还没有加过油)")
    private Date refuelTime;

    @Schema(description="所属车队")
    private String motorcadeName;

    @Schema(description="所属车队")
    private String motorcadeId;


}
