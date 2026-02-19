package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.ItemSku;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ItemSku.class, reverseConvertGenerate = false)
public class ItemSkuDto extends BaseEntity {

    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    @Schema(description = "id")
    @NotBlank(message = "规格名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String skuName;

    @Schema(description = "id")
    @NotNull(message = "物品id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long itemId;

    @Schema(description = "sku条码")
    private String barcode;

    @Schema(description = "编码")
    private String skuNo;

    @Schema(description = "长(cm)")
    private BigDecimal length;

    @Schema(description = "宽(cm)")
    private BigDecimal width;

    @Schema(description = "高(cm)")
    private BigDecimal height;

    @Schema(description = "毛重(kg)")
    private BigDecimal grossWeight;

    @Schema(description = "净重(kg)")
    private BigDecimal netWeight;

    @Schema(description = "单价($)")
    private BigDecimal unitPrice;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品编码")
    private String itemNo;

    @Schema(description = "物品分类")
    private String itemCategory;

    @Schema(description = "")
    private Long itemBrand;
}
