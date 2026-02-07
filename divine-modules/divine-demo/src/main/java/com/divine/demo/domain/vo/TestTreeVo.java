package com.divine.demo.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.demo.domain.entity.TestTree;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * 测试树表视图对象 test_tree
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestTree.class)
public class TestTreeVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父id")
    @ExcelProperty(value = "父id")
    private Long parentId;

    @Schema(description = "部门id")
    @ExcelProperty(value = "部门id")
    private Long deptId;

    @Schema(description = "用户id")
    @ExcelProperty(value = "用户id")
    private Long userId;

    @Schema(description = "树节点名")
    @ExcelProperty(value = "树节点名")
    private String treeName;

    @Schema(description = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date createTime;


}
