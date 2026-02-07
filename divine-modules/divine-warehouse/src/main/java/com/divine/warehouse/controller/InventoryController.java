package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.InventoryDto;
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
import com.divine.warehouse.domain.vo.InventoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.InventoryService;

import java.util.List;

/**
 * 库存
 *
 * @author zcc
 * @date 2024-07-19
 */
@Tag(name = "库存")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController extends BaseController {

    private final InventoryService inventoryService;

    /**
     * 查询库存列表商品维度
     */
    @Operation(summary = "查询库存列表商品维度")
    @SaCheckPermission("wms:inventory:all")
    @GetMapping(value = {"/boardList/item"})
    public PageInfoRes<InventoryVo> queryItemBoardList(InventoryDto bo, BasePage basePage) {
        return inventoryService.queryItemBoardList(bo, basePage);
    }

    /**
     * 查询库存列表仓库维度
     */
    @Operation(summary = "查询库存列表仓库维度")
    @SaCheckPermission("wms:inventory:all")
    @GetMapping("/boardList/warehouse")
    public PageInfoRes<InventoryVo> queryWarehouseBoardList(InventoryDto bo, BasePage basePage) {
        return inventoryService.queryWarehouseBoardList(bo, basePage);
    }

    /**
     * 查询库存列表商品维度
     */
    @Operation(summary = "查询库存列表商品维度")
    @SaCheckPermission("wms:inventory:all")
    @GetMapping(value = {"/listNoPage"})
    public Result<List<InventoryVo>> listNoPage(InventoryDto bo) {
        return Result.success(inventoryService.queryList(bo));
    }

    /**
     * 导出库存列表
     */
    @Operation(summary = "导出库存列表")
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InventoryDto bo, HttpServletResponse response) {
        List<InventoryVo> list = inventoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存", InventoryVo.class, response);
    }

    /**
     * 获取库存详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取库存详细信息")
    @SaCheckPermission("wms:inventory:all")
    @GetMapping("/{id}")
    public Result<InventoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(inventoryService.queryById(id));
    }

    /**
     * 新增库存
     */
    @Operation(summary = "新增库存")
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody InventoryDto bo) {
        inventoryService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改库存
     */
    @Operation(summary = "修改库存")
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody InventoryDto bo) {
        inventoryService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除库存
     *
     * @param ids 主键串
     */
    @Operation(summary = "删除库存")
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        inventoryService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
