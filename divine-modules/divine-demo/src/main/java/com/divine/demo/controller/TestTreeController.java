package com.divine.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.demo.domain.vo.TestTreeVo;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.core.validate.QueryGroup;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.web.core.BaseController;
import com.divine.demo.domain.dto.TestTreeDto;
import com.divine.demo.service.ITestTreeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Tag(name = "测试树表Controller")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/tree")
public class TestTreeController extends BaseController {

    private final ITestTreeService iTestTreeService;

    @Operation(summary = "查询测试树表列表")
    @SaCheckPermission("demo:tree:list")
    @GetMapping("/list")
    public Result<List<TestTreeVo>> list(@Validated(QueryGroup.class) TestTreeDto bo) {
        List<TestTreeVo> list = iTestTreeService.queryList(bo);
        return Result.success(list);
    }

    @Operation(summary = "导出测试树表列表")
    @SaCheckPermission("demo:tree:export")
    @Log(title = "测试树表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(@Validated TestTreeDto bo, HttpServletResponse response) {
        List<TestTreeVo> list = iTestTreeService.queryList(bo);
        ExcelUtil.exportExcel(list, "测试树表", TestTreeVo.class, response);
    }

    @Operation(summary = "获取测试树表详细信息")
    @SaCheckPermission("demo:tree:query")
    @GetMapping("/{id}")
    public Result<TestTreeVo> getInfo(@NotNull(message = "主键不能为空")
                                      @PathVariable("id") Long id) {
        return Result.success(iTestTreeService.queryById(id));
    }

    @Operation(summary = "新增测试树表")
    @SaCheckPermission("demo:tree:add")
    @Log(title = "测试树表", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody TestTreeDto bo) {
        return toAjax(iTestTreeService.insertByBo(bo));
    }

    @Operation(summary = "修改测试树表")
    @SaCheckPermission("demo:tree:edit")
    @Log(title = "测试树表", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody TestTreeDto bo) {
        return toAjax(iTestTreeService.updateByBo(bo));
    }

    @Operation(summary = "删除测试树表")
    @SaCheckPermission("demo:tree:remove")
    @Log(title = "测试树表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                               @PathVariable Long[] ids) {
        return toAjax(iTestTreeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
