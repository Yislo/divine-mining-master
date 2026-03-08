package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.divine.warehouse.domain.entity.CheckOrder;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存盘点单据视图对象 wms_check_order
 *
 * @author yisl
 * @date 2024-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ExcelIgnoreUnannotated
@AutoMapper(target = CheckOrder.class)
public class CheckOrderVo extends BaseOrderVo<CheckOrderDetailVO> {

    /**
     * 盘库单号
     */
    private String checkNo;

    /**
     * 盘库单状态
     */
    private Integer checkStatus;

}
