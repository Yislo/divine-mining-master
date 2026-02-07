package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.ItemDto;
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
import com.divine.warehouse.domain.vo.ItemVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.ItemService;

import java.util.List;

@Tag(name = "物品列表")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController extends BaseController {

    private final ItemService itemService;

    /**
     * 查询物料列表
     */
    @Operation(summary = "查询物料列表")
    @GetMapping("/list")
    @SaCheckPermission("wms:item:list")
    public PageInfoRes<ItemVo> list(ItemDto bo, BasePage basePage) {
        return itemService.queryPageList(bo, basePage);
    }

    /**
     * 查询物料列表
     */
    @Operation(summary = "查询物料列表")
    @GetMapping("/listNoPage")
    @SaCheckPermission("wms:item:list")
    public Result<List<ItemVo>> list(ItemDto bo) {
        return Result.success(itemService.queryList(bo));
    }

    /**
     * 导出物料列表
     */
    @Operation(summary = "导出物料列表")
    @Log(title = "物料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:item:list")
    public void export(ItemDto bo, HttpServletResponse response) {
        List<ItemVo> list = itemService.queryList(bo);
        ExcelUtil.exportExcel(list, "物料", ItemVo.class, response);
    }

    /**
     * 获取物料详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取物料详细信息")
    @GetMapping("/{id}")
    @SaCheckPermission("wms:item:list")
    public Result<ItemVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(itemService.queryById(id));
    }

    /**
     * 新增物料
     */
    @Operation(summary = "新增物料")
    @Log(title = "物料", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemDto form) {
        itemService.insertByForm(form);
        return Result.success();
    }
    /**
     * 修改物料
     */
    @Operation(summary = "修改物料")
    @Log(title = "物料", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemDto form) {
        itemService.updateByForm(form);
        return Result.success();
    }

    /**
     * 删除物料
     *
     * @param id 主键
     */
    @Operation(summary = "删除物料")
    @Log(title = "物料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        itemService.deleteById(id);
        return Result.success();
    }
}
