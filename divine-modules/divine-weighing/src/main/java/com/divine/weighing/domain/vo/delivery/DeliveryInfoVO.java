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
@Schema(description="送货记录相关出参")
public class DeliveryInfoVO implements Serializable{

	@Schema(description="id")
	private String id;

	@Schema(description="车牌号")
	private String carNumber;

	@Schema(description="矿主名称")
	private String ownerName;

	@Schema(description="目的地工厂id")
	private String companyId;

	@Schema(description="总重(kg)")
	private String totalWeight;

	@Schema(description="净重(kg)")
	private String netWeight;

	@Schema(description="空重(kg)")
	private String emptyWeight;

	@Schema(description="金吨-仅管理员可见")
	private String goldTon;

	@Schema(description="编号")
	private String carCode;

	@Schema(description="所属车队")
	private String motorcadeName;

	@Schema(description="送货状态(0:已发车,1:已过磅,2:已完成)")
	private Integer status;

	@Schema(description="送货状态文本展示")
	private String statusText;

//	@Schema(description="相关文件")
//	private List<FileVO> fileList;

	@Schema(description="发车时间")
	private Date departureTime;

	@Schema(description="过磅时间")
	private Date weighingTime;

	@Schema(description="操作人")
	private String createByName;

	@Schema(description="备注")
	private String remark;

	@Schema(description="备注")
	private List<String> remarkList;
	public static void fillDefaultValues(DeliveryInfoVO info) {
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
