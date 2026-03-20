package com.divine.mine.domain.dto;

import com.divine.common.mybatis.core.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/8 11:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QualityPageDTO extends BasePage {

    /**
     * 质量编号
     */
    private String qualityNo;

    /**
     * 过磅单编号
     */
    private String weightingNo;

    /**
     * 送货单位
     */
    private Long shipMerchantId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


}
