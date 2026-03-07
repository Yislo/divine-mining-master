package com.divine.system.domain.vo.home;

import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:32
 */
@Data
public class WeightingInfoVO {

    /**
     * 过磅数量
     */
    private Long weightingNum;

    /**
     * 过磅类型(1:矿车,2:硫酸)
     */
    private Integer weightingType;
}
