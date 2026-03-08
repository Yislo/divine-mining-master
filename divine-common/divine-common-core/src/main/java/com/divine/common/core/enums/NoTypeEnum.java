package com.divine.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/8 9:28
 */
@Getter
@AllArgsConstructor
public enum NoTypeEnum {

    RECEIPT_NO(1, "RK", "入库单编号"),
    SHIPMENT_NO(2, "CK", "出库单编号"),
    MOVE_NO(3, "YK", "移库单编号"),
    CHECK_NO(4, "PK", "盘库单编号"),
    REFUEL_NO(5, "JY", "加油单编号"),
    SKU_NO(6, "SKU", "规格编号"),
    SPU_NO(7, "SPU", "物品编编号"),
    WEIGHING_NO(8, "GB", "过磅单编号"),
    QUALITY_NO(9, "ZL", "质量编号"),

    ;

    private final Integer type;
    private final String code;
    private final String desc;


}

