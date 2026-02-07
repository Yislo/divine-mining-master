package com.divine.weighing.domain.vo.delivery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 发车记录相关出参
 * @Author: yisl
 * @Date: 2025-10-29 18:56:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "送货记录相关出参")
public class DeliveryCarInfoVO implements Serializable {

    @Schema(description = "id")
    private String id;

    @Schema(description = "过磅编号")
    private String weightNo;

    @Schema(description = "车牌号")
    private String carNumber;

    @Schema(description = "目的地工厂")
    private String companyName;

    @Schema(description = "矿主名称")
    private String ownerName;

    @Schema(description = "所属车队")
    private String motorcadeName;

    @Schema(description = "工厂磅单编号")
    private String companyWeightNo;

    @Schema(description = "送货状态(0:已发车,1:已过磅,2:已完成)")
    private Integer status;

    @Schema(description = "送货状态文本展示")
    private String statusText;

    @Schema(description = "防伪码")
    private String antiFakeCode;

    @Schema(description = "组货结算运费")
    private String settlePrice;

    @Schema(description = "金吨-仅管理员可见")
    private String goldTon;

    @Schema(description = "组货结算时间")
    private Date settleTime;

    @Schema(description = "开票日期")
    private Date ticketTime;

    @Schema(description = "授权日期")
    private Date accreditTime;

    @Schema(description = "发车时间")
    private Date departureTime;

    @Schema(description = "过磅时间")
    private Date weighingTime;

    @Schema(description = "操作人")
    private String createByName;
//
//    @Schema(description = "批次号")
//    private String batchCode;

    @Schema(description = "目的地工厂id")
    private String companyId;

    @Schema(description = "总重(kg)")
    private String totalWeight;

    @Schema(description = "净重(kg)")
    private String netWeight;

    @Schema(description = "空重(kg)")
    private String emptyWeight;

    @Schema(description = "水份")
    private String moisture;

    @Schema(description = "品位")
    private String cuoRatio;

    @Schema(description = "酸耗")
    private String acidDemand;
//
//    @Schema(description = "相关文件")
//    private List<FileVO> fileList;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "备注")
    private List<String> remarkList;

    public static void fillDefaultValues(DeliveryCarInfoVO info) {
        info.setTotalWeight(defaultIfBlankOrZero(info.getTotalWeight()));
        info.setNetWeight(defaultIfBlankOrZero(info.getNetWeight()));
        info.setEmptyWeight(defaultIfBlankOrZero(info.getEmptyWeight()));
    }

    private static String defaultIfBlankOrZero(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equals("0")) {
            return "-";
        }
        return value;
    }

}
