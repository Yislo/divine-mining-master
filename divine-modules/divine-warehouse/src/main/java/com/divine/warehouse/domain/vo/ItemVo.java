package com.divine.warehouse.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.warehouse.domain.entity.Item;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Item.class)
public class ItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @ExcelProperty(value = "id")
    private Long id;

    @Schema(description = "编号")
    @ExcelProperty(value = "编号")
    private String itemNo;

    @Schema(description = "名称")
    @ExcelProperty(value = "名称")
    private String itemName;

    @Schema(description = "分类")
    @ExcelProperty(value = "分类")
    private String itemCategory;

    @Schema(description = "单位类别")
    @ExcelProperty(value = "单位类别")
    private String unit;

    @Schema(description = "品牌")
    @ExcelProperty(value = "品牌")
    private Long itemBrand;

    @Schema(description = "备注")
    @ExcelProperty(value = "备注")
    private String remark;

    @Schema(description = "类别")
    private ItemCategoryVo itemCategoryInfo;
}
