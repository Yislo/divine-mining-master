package com.divine.system.domain.vo.home;

import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:23
 */

@Data
public class WarehouseInfoVO {

    /**
     * 入库数量
     */
    private Long receiptNum;

    /**
     * 出库数量
     */
    private Long shipmentNum;

    /**
     * 库存预警数量
     */
    private Long warningNum;
}
