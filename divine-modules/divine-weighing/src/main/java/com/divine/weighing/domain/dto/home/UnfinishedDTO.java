package com.divine.weighing.domain.dto.home;

import com.divine.common.mybatis.core.page.BasePage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/20 20:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnfinishedDTO extends BasePage {
//
//    @Schema(description="开始时间")
//    private Date startTime;
//
//    @Schema(description="结束时间")
//    private Date endTime;
//
//    @Schema(description="送货状态(0:已发车,1:已过磅,2:已完成)")
//    private Integer deliverStatus;

    @Schema(description="车牌号")
    private String carNumber;


}
