package com.divine.weighing.export;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExportContext {


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
    private Date settleTime;
    /**
     * 开票日期
     */
    private Date ticketTime;
    /**
     * 授权日期
     */
    private Date accreditTime;
    /**
     * 发车时间
     */
    private Date departureTime;
    /**
     * 过磅时间
     */
    private Date weighingTime;
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
    /**
     * 金吨
     */
    private Integer goldTon;
}
