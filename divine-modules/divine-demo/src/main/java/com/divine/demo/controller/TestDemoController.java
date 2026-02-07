package com.divine.demo.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.demo.domain.dto.TestDemoDto;
import com.divine.demo.domain.entity.TestDemo;
import com.divine.demo.domain.vo.TestDemoVo;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.ValidatorUtils;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.core.validate.QueryGroup;
import com.divine.common.excel.core.ExcelResult;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.web.core.BaseController;
import com.divine.demo.domain.dto.TestDemoImportDto;
import com.divine.demo.service.ITestDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "测试单表Controller")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/demo")
public class TestDemoController extends BaseController {

    private final ITestDemoService iTestDemoService;

    @Operation(summary = "查询测试单表列表")
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/list")
    public PageInfoRes<TestDemoVo> list(@Validated(QueryGroup.class) TestDemoDto bo, BasePage basePage) {
        return iTestDemoService.queryPageList(bo, basePage);
    }

    @Operation(summary = "自定义分页查询")
    @SaCheckPermission("demo:demo:list")
    @GetMapping("/page")
    public PageInfoRes<TestDemoVo> page(@Validated(QueryGroup.class) TestDemoDto bo, BasePage basePage) {
        return iTestDemoService.customPageList(bo, basePage);
    }

    @Operation(summary = "导入数据")
    @Log(title = "测试单表", businessType = BusinessType.IMPORT)
    @SaCheckPermission("demo:demo:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Void> importData(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<TestDemoImportDto> excelResult = ExcelUtil.importExcel(file.getInputStream(), TestDemoImportDto.class, true);
        List<TestDemoImportDto> volist = excelResult.getList();
        List<TestDemo> list = MapstructUtils.convert(volist, TestDemo.class);
        iTestDemoService.saveBatch(list);
        return Result.success(excelResult.getAnalysis());
    }

    @Operation(summary = "导出测试单表列表")
    @SaCheckPermission("demo:demo:export")
    @Log(title = "测试单表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated TestDemoDto bo, HttpServletResponse response) {
        List<TestDemoVo> list = iTestDemoService.queryList(bo);
        // 测试雪花id导出
//        for (TestDemoVo vo : list) {
//            vo.setId(1234567891234567893L);
//        }
        ExcelUtil.exportExcel(list, "测试单表", TestDemoVo.class, response);
    }

    @Operation(summary = "获取测试单表详细信息")
    @SaCheckPermission("demo:demo:query")
    @GetMapping("/{id}")
    public Result<TestDemoVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable("id") Long id) {
        return Result.success(iTestDemoService.queryById(id));
    }

    @Operation(summary = "新增测试单表")
    @SaCheckPermission("demo:demo:add")
    @Log(title = "测试单表", businessType = BusinessType.INSERT)
    @RepeatSubmit(interval = 2, timeUnit = TimeUnit.SECONDS, message = "{repeat.submit.message}")
    @PostMapping()
    public Result<Void> add(@RequestBody TestDemoDto bo) {
        // 使用校验工具对标 @Validated(AddGroup.class) 注解
        // 用于在非 Controller 的地方校验对象
        ValidatorUtils.validate(bo, AddGroup.class);
        return toAjax(iTestDemoService.insertByBo(bo));
    }

    @Operation(summary = "修改测试单表")
    @SaCheckPermission("demo:demo:edit")
    @Log(title = "测试单表", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody TestDemoDto bo) {
        return toAjax(iTestDemoService.updateByBo(bo));
    }

    @Operation(summary = "删除测试单表")
    @SaCheckPermission("demo:demo:remove")
    @Log(title = "测试单表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iTestDemoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
