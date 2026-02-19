package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/12 19:51
 */
@Data
public class BoardListVO {

    @ExcelProperty(value = "仓库id")
    @Schema(description = "仓库id")
    private Long warehouseId;

    @ExcelProperty(value = "仓库名称")
    @Schema(description = "仓库名称")
    private String warehouseName;

    @ExcelProperty(value = "物品id")
    @Schema(description = "物品id")
    private Long itemId;

    @ExcelProperty(value = "物品名称")
    @Schema(description = "物品名称")
    private String itemName;

    @ExcelProperty(value = "物品编号")
    @Schema(description = "物品编号")
    private String itemNo;

    @Schema(description = "skuId")
    private Long skuId;

    @Schema(description = "sku名称")
    private String skuName;

    @Schema(description = "sku编号")
    private String skuNo;

    @Schema(description = "库存信息")
    private String stockInfoText;

    @Schema(description = "库存信息")
    private List<StorageShelfVO> stockInfo;


}
