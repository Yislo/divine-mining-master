package com.divine.warehouse.domain.dto;

import com.divine.warehouse.domain.entity.CheckOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存盘点单据详情业务对象 wms_check_order_detail
 *
 * @author zcc
 * @date 2024-08-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CheckOrderDetail.class, reverseConvertGenerate = false)
public class CheckOrderDetailDto extends BaseOrderDetailDto {

    /**
     * 盘点数量
     */
    @Schema(description = "盘点数量")
    private BigDecimal checkQuantity;

    /**
     * 盈亏数
     */
    @Schema(description = "盈亏数")
    private BigDecimal profitAndLoss;

    /**
     * 库存id
     */
    @Schema(description = "库存id")
    private Long inventoryId;

    /**
     * 有盈亏
     */
    @Schema(description = "有盈亏")
    private Boolean haveProfitAndLoss;
}
