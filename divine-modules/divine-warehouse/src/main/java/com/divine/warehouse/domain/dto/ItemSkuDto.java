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

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规格名称
     */
    @Schema(description = "id")
    @NotBlank(message = "规格名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String skuName;

    /**
     * 物品id
     */
    @Schema(description = "id")
    @NotNull(message = "物品id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long itemId;

    /**
     * sku条码
     */
    @Schema(description = "sku条码")
    private String barcode;

    /**
     * 编码
     */
    @Schema(description = "编码")
    private String skuCode;

    /**
     * 长(cm)
     */
    @Schema(description = "长(cm)")
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    @Schema(description = "宽(cm)")
    private BigDecimal width;

    /**
     * 高(cm)
     */
    @Schema(description = "高(cm)")
    private BigDecimal height;

    /**
     * 毛重(kg)
     */
    @Schema(description = "毛重(kg)")
    private BigDecimal grossWeight;

    /**
     * 净重(kg)
     */
    @Schema(description = "净重(kg)")
    private BigDecimal netWeight;

    /**
     * 成本价($)
     */
    @Schema(description = "成本价($)")
    private BigDecimal costPrice;

    /**
     * 销售价($)
     */
    @Schema(description = "销售价($)")
    private BigDecimal sellingPrice;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称")
    private String itemName;

    /**
     * 商品编码
     */
    @Schema(description = "商品编码")
    private String itemCode;

    /**
     * 商品分类
     */
    @Schema(description = "商品分类")
    private String itemCategory;

    @Schema(description = "")
    private Long itemBrand;
}
