package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.web.core.BaseController;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.warehouse.service.ItemSkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "物品sku")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/itemSku")
public class ItemSkuController extends BaseController {

    private final ItemSkuService itemSkuService;

    /**
     * 查询sku信息列表
     */
    @Operation(summary = "查询sku信息列表")
    @GetMapping("/list")
    @SaCheckPermission("wms:item:list")
    public PageInfoRes<ItemSkuMapVo> list(ItemSkuDto bo, BasePage basePage) {
        return itemSkuService.queryPageList(bo, basePage);
    }
    /**
     * 查询sku信息列表
     */
    @Operation(summary = "查询sku信息列表")
    @GetMapping("/listNoPage")
    @SaCheckPermission("wms:item:list")
    public Result<List<ItemSkuVo>> list(ItemSkuDto bo) {
        return Result.success(itemSkuService.queryList(bo));
    }

    /**
     * 导出sku信息列表
     */
    @Operation(summary = "导出sku信息列表")
    @Log(title = "sku信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:item:list")
    public void export(ItemSkuDto bo, HttpServletResponse response) {
        List<ItemSkuVo> list = itemSkuService.queryList(bo);
        ExcelUtil.exportExcel(list, "sku信息", ItemSkuVo.class, response);
    }

    /**
     * 获取sku信息详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取sku信息详细信息")
    @GetMapping("/{id}")
    @SaCheckPermission("wms:item:list")
    public Result<ItemSkuVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(itemSkuService.queryById(id));
    }

    /**
     * 新增sku信息
     */
    @Operation(summary = "新增sku信息")
    @Log(title = "sku信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemSkuDto bo) {
        return toAjax(itemSkuService.insertByBo(bo));
    }

    /**
     * 修改sku信息
     */
    @Operation(summary = "修改sku信息")
    @Log(title = "sku信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemSkuDto bo) {
        return toAjax(itemSkuService.updateByBo(bo));
    }

    /**
     * 删除sku信息
     *
     * @param id 主键
     */
    @Operation(summary = "删除sku信息")
    @Log(title = "sku信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        itemSkuService.deleteById(id);
        return Result.success();
    }
}
