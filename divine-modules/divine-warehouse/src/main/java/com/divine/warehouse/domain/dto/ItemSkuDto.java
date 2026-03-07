package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.ItemSku;
import io.github.linpeilie.annotations.AutoMapper;
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
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String skuName;

    /**
     * id
     */
    @NotNull(message = "物品id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long itemId;

//    /**
//     * sku条码
//     */
//    private String barcode;

    /**
     * sku编号
     */
    private String skuNo;

    /**
     * 长(cm)
     */
    private BigDecimal length;

    /**
     * 宽(cm)
     */
    private BigDecimal width;

    /**
     * 高(cm)
     */
    private BigDecimal height;

    /**
     * 毛重(kg)
     */
    private BigDecimal grossWeight;

    /**
     * 净重(kg)
     */
    private BigDecimal netWeight;

    /**
     * 单价($)
     */
    private BigDecimal unitPrice;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品编码
     */
    private String itemNo;

    /**
     * 物品分类
     */
    private String itemCategory;

    /**
     * 品牌
     */
    private Long itemBrand;
}
