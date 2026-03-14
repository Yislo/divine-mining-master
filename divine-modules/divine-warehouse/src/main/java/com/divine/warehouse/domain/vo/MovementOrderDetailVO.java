package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存移动详情视图对象 wms_movement_order_detail
 *
 * @author yisl
 * @date 2024-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = MovementOrderDetail.class)
public class MovementOrderDetailVO extends BaseOrderDetailVO {

    /**
     * 源仓库
     */
    @ExcelProperty(value = "源仓库")
    private Long sourceWarehouseId;

    /**
     * 目标仓库
     */
    @ExcelProperty(value = "目标仓库")
    private Long targetWarehouseId;

    /**
     * 源货架
     */
    @NotNull(message = "源货架不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sourceStorageShelf;

    /**
     * 目标货架
     */
    @NotNull(message = "目标货架不能为空", groups = { AddGroup.class, EditGroup.class })
    private String targetStorageShelf;

    /**
     * 剩余数量
     */
    private Long remainQuantity;
}
