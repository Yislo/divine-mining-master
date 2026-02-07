package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.log.annotation.Log;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.dto.SysDictTypeDto;
import com.divine.system.domain.vo.SysDictTypeVo;
import com.divine.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "数据字典类型")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    private final SysDictTypeService dictTypeService;

    @Operation(summary = "查询字典类型列表")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public PageInfoRes<SysDictTypeVo> list(SysDictTypeDto dictType, BasePage basePage) {
        return dictTypeService.selectPageDictTypeList(dictType, basePage);
    }

    @Operation(summary = "导出字典类型列表")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(SysDictTypeDto dictType, HttpServletResponse response) {
        List<SysDictTypeVo> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil.exportExcel(list, "字典类型", SysDictTypeVo.class, response);
    }

    @Operation(summary = "查询字典类型详细")
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public Result<SysDictTypeVo> getInfo(@PathVariable Long dictId) {
        return Result.success(dictTypeService.selectDictTypeById(dictId));
    }

    @Operation(summary = "新增字典类型")
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysDictTypeDto dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.insertDictType(dict);
        return Result.success();
    }

    @Operation(summary = "修改字典类型")
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysDictTypeDto dict) {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return Result.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dictTypeService.updateDictType(dict);
        return Result.success();
    }

    @Operation(summary = "删除字典类型")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public Result<Void> remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return Result.success();
    }

    @Operation(summary = "刷新字典缓存")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Result<Void> refreshCache() {
        dictTypeService.resetDictCache();
        return Result.success();
    }

    @Operation(summary = "获取字典选择框列表")
    @GetMapping("/optionselect")
    public Result<List<SysDictTypeVo>> optionselect() {
        List<SysDictTypeVo> dictTypes = dictTypeService.selectDictTypeAll();
        return Result.success(dictTypes);
    }
}
