package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysDictDataDto;
import com.divine.common.log.annotation.Log;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.vo.SysDictDataVo;
import com.divine.system.service.SysDictDataService;
import com.divine.system.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "数据字典信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    private final SysDictDataService dictDataService;
    private final SysDictTypeService dictTypeService;

    @Operation(summary = "查询字典数据列表")
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public PageInfoRes<SysDictDataVo> list(SysDictDataDto dictData, BasePage basePage) {
        return dictDataService.selectPageDictDataList(dictData, basePage);
    }

    /**
     * 导出字典数据列表
     */
    @Operation(summary = "导出字典数据列表")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(SysDictDataDto dictData, HttpServletResponse response) {
        List<SysDictDataVo> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil.exportExcel(list, "字典数据", SysDictDataVo.class, response);
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     */
    @Operation(summary = "查询字典数据详细")
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public Result<SysDictDataVo> getInfo(@PathVariable Long dictCode) {
        return Result.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public Result<List<SysDictDataVo>> dictType(@PathVariable String dictType) {
        List<SysDictDataVo> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return Result.success(data);
    }

    @Operation(summary = "新增字典类型")
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysDictDataDto dict) {
        if (!dictDataService.checkDictDataUnique(dict)) {
            return Result.fail("新增字典数据'" + dict.getDictValue() + "'失败，字典键值已存在");
        }
        dictDataService.insertDictData(dict);
        return Result.success();
    }

    @Operation(summary = "修改保存字典类型")
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysDictDataDto dict) {
        if (!dictDataService.checkDictDataUnique(dict)) {
            return Result.fail("修改字典数据'" + dict.getDictValue() + "'失败，字典键值已存在");
        }
        dictDataService.updateDictData(dict);
        return Result.success();
    }

    /**
     * 删除字典类型
     *
     * @param dictCodes 字典code串
     */
    @Operation(summary = "删除字典类型")
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public Result<Void> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return Result.success();
    }
}
