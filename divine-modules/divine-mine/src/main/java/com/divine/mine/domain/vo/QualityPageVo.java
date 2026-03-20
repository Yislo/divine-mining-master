package com.divine.mine.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/8 11:16
 */
@Data
public class QualityPageVo {

    /**
     * id
     */
    private Long id;

    /**
     * 质量编号
     */
    private String qualityNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 送货单位
     */
    private String shipMerchantId;

    /**
     * 送货单位名称
     */
    private String shipMerchantName;

    /**
     * 品位
     */
    private BigDecimal cuoRatio;

    /**
     * 水份
     */
    private BigDecimal moisture;

    /**
     * 酸耗
     */
    private BigDecimal acidDemand;

    /**
     * 取样时间
     */
    private LocalDateTime createTime;

    /**
     * 备注
     */
    private String remark;


}
