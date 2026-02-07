package com.divine.demo.domain.dto;

import com.divine.demo.domain.entity.TestTree;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 测试树表业务对象 test_tree
 *
 * @author Lion Li
 * @date 2021-07-26
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = TestTree.class)
public class TestTreeDto extends BaseEntity {

    @Schema(description = "主键")
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    @Schema(description = "父ID")
    private Long parentId;

    @Schema(description = "部门id")
    @NotNull(message = "部门id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deptId;

    @Schema(description = "用户id")
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;

    @Schema(description = "树节点名")
    @NotBlank(message = "树节点名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String treeName;

}
