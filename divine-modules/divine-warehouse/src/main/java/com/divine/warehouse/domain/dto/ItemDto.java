package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.Item;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Item.class, reverseConvertGenerate = false)
public class ItemDto extends BaseEntity {

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 主键集合
     */
    @Schema(description = "主键集合")
    private List<Long> ids;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String itemNo;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemName;

    /**
     * 分类
     */
    @Schema(description = "分类")
    @NotBlank(message = "分类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemCategory;

    /**
     * 单位类别
     */
    @Schema(description = "单位类别")
    private String unit;

    /**
     * 品牌
     */
    @Schema(description = "品牌")
    private Long itemBrand;


    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "sku")
    private List<ItemSkuDto> sku;

}
