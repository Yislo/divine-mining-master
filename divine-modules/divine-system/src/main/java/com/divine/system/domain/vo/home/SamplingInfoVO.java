package com.divine.system.domain.vo.home;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:39
 */
@Data
public class SamplingInfoVO {

    /**
     * 取样时间
     */
    private Long samplingNum;

    /**
     * 取样数量
     */
    private LocalDateTime samplingTime;

}
