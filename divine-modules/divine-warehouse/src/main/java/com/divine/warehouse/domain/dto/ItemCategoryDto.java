package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.ItemCategory;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ItemCategory.class, reverseConvertGenerate = false)
public class ItemCategoryDto extends BaseEntity {

    /**
     * 物料类型id
     */
    @Schema(description = "物料类型id")
    @NotNull(message = "物料类型id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 父物料类型id
     */
    @Schema(description = "父物料类型id")
    private Long parentId;

    /**
     * 物料类型名称
     */
    @Schema(description = "物料类型名称")
    @NotBlank(message = "物料类型名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String categoryName;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序")
    private Long orderNum;

    /**
     * 物料类型状态（0停用 1正常）
     */
    @Schema(description = "物料类型状态（0停用 1正常）")
    private String status;


}
