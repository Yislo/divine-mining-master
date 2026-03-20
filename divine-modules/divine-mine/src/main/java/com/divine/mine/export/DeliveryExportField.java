package com.divine.mine.export;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DeliveryExportField {
    /**
     * 送货ID
     */
    private String id;
    /**
     * 过磅编号
     */
    private String weightNo;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 工厂磅单编号
     */
    private String companyWeightNo;
    /**
     * 防伪码
     */
    private String antiFakeCode;
    /**
     * 送货状态
     */
    private String statusText;
    /**
     * 组货结算运费
     */
    private BigDecimal settlePrice;
    /**
     * 组货结算日期
     */
    private LocalDateTime settleTime;
    /**
     * 开票日期
     */
    private LocalDateTime ticketTime;
    /**
     * 授权日期
     */
    private LocalDateTime accreditTime;
    /**
     * 发车时间
     */
    private LocalDateTime departureTime;
    /**
     * 过磅时间
     */
    private LocalDateTime weighingTime;
    /**
     * 工厂名称
     */
    private String companyName;
    /**
     * 矿主名称
     */
    private String ownerName;
    /**
     * 车队名称
     */
    private String motorcadeName;
    /**
     * 总重(kg)
     */
    private Integer totalWeight;
    /**
     * 净重(kg)
     */
    private Integer netWeight;
    /**
     * 空重(kg)
     */
    private Integer emptyWeight;
    /**
     * 水份
     */
    private BigDecimal moisture;
    /**
     * 氧化铜品位
     */
    private BigDecimal cuoRatio;
    /**
     * 酸耗
     */
    private BigDecimal acidDemand;
}
