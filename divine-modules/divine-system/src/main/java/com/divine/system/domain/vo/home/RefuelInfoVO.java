package com.divine.system.domain.vo.home;

import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:57
 */
@Data
public class RefuelInfoVO {

    /**
     * 内部车柴油(L)
     */
    private Long interiorDiesel;

    /**
     * 内部车汽油(L)
     */
    private Long interiorGasoline;

    /**
     * 外部车汽油(L)
     */
    private Long externalDiesel;

    /**
     * 外部车汽油(L)
     */
    private Long externalGasoline;
}
