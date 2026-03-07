package com.divine.warehouse.domain.dto;

import com.divine.warehouse.domain.entity.CheckOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存盘点单据详情业务对象 wms_check_order_detail
 *
 * @author yisl
 * @date 2024-08-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = CheckOrderDetail.class, reverseConvertGenerate = false)
public class CheckOrderDetailDto extends BaseOrderDetailDto {

    /**
     * 盘库单id
     */
    private Long checkId;

    /**
     * 盘点数量
     */
    private Long checkQuantity;

    /**
     * 盈亏数
     */
    private Long profitAndLoss;

    /**
     * 库存id
     */
    private Long inventoryId;

    /**
     * 有盈亏
     */
    private Boolean haveProfitAndLoss;
}
