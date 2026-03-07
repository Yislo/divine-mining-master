package com.divine.system.domain.vo.home;

import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:43
 */
@Data
public class SamplingMerchantVO {

    /**
     * 取样数量
     */
    private Long samplingNum;

    /**
     * 客户Id
     */
    private String merchantId;

    /**
     * 客户名称
     */
    private String merchantName;

}
