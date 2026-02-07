package com.divine.demo.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.demo.domain.entity.TestDemo;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;


/**
 * 测试单表视图对象 test_demo
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestDemo.class)
public class TestDemoVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @ExcelProperty(value = "主键")
    private Long id;

    @Schema(description = "部门id")
    @ExcelProperty(value = "部门id")
    private Long deptId;

    @Schema(description = "用户id")
    @ExcelProperty(value = "用户id")
    private Long userId;

    @Schema(description = "排序号")
    @ExcelProperty(value = "排序号")
    private Integer orderNum;

    @Schema(description = "key键")
    @ExcelProperty(value = "key键")
    private String testKey;

    @Schema(description = "值")
    @ExcelProperty(value = "值")
    private String value;

    @Schema(description = "创建时间")
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @Schema(description = "创建人")
    @ExcelProperty(value = "创建人")
    private String createBy;

    @Schema(description = "更新时间")
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    @Schema(description = "更新人")
    @ExcelProperty(value = "更新人")
    private String updateBy;


}
