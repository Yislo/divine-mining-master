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
public class RecentInfoVO implements Serializable{

    @Schema(description="id")
    private String id;

    @Schema(description="主题")
    private String subject;

    @Schema(description="内容")
    private String content;

    @Schema(description="消息类型(0:新增发车记录,1:车辆过磅,2:新增加油记录,3:新增入库记录)")
    private Integer type;

    @Schema(description="操作人")
    private String createUser;

    @Schema(description="时间")
    private Date createTime;

}
