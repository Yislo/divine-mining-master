package com.divine.warehouse.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@ExcelIgnoreUnannotated
public class ReceiptImportDTO {

    /**
     * 仓库id
     */
    private Long warehouseId;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * 规格
     */
    private String skuName;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;

    /**
     * 货架
     */
    private String storageShelf;

    /**
     * 备注
     */
    private String remark;


    public String getSkuName(){
        return StringUtils.isBlank(skuName) ? "-" : skuName;
    }

}
