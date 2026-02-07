package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.warehouse.domain.entity.Warehouse;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Warehouse.class, reverseConvertGenerate = false)
public class WarehouseDto extends BaseEntity {

    /**
     *
     */
    @Schema(description = "id")
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String warehouseCode;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String warehouseName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Long orderNum;
}
