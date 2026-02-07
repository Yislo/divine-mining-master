package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.InventoryHistoryDto;
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
import com.divine.warehouse.domain.vo.InventoryHistoryVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.InventoryHistoryService;

import java.util.List;

/**
 * 库存记录
 *
 * @author zcc
 * @date 2024-07-22
 */
@Tag(name = "库存记录")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inventoryHistory")
public class InventoryHistoryController extends BaseController {

    private final InventoryHistoryService inventoryHistoryService;

    /**
     * 查询库存记录列表
     */
    @Operation(summary = "查询库存记录列表")
    @SaCheckPermission("wms:inventoryHistory:all")
    @GetMapping("/list")
    public PageInfoRes<InventoryHistoryVo> list(InventoryHistoryDto bo, BasePage basePage) {
        return inventoryHistoryService.queryPageList(bo, basePage);
    }

    /**
     * 导出库存记录列表
     */
    @Operation(summary = "导出库存记录列表")
    @SaCheckPermission("wms:inventoryHistory:all")
    @Log(title = "库存记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InventoryHistoryDto bo, HttpServletResponse response) {
        List<InventoryHistoryVo> list = inventoryHistoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存记录", InventoryHistoryVo.class, response);
    }

    /**
     * 获取库存记录详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取库存记录详细信息")
    @SaCheckPermission("wms:inventoryHistory:all")
    @GetMapping("/{id}")
    public Result<InventoryHistoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(inventoryHistoryService.queryById(id));
    }

    /**
     * 新增库存记录
     */
    @Operation(summary = "新增库存记录")
    @SaCheckPermission("wms:inventoryHistory:all")
    @Log(title = "库存记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody InventoryHistoryDto bo) {
        inventoryHistoryService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改库存记录
     */
    @Operation(summary = "修改库存记录")
    @SaCheckPermission("wms:inventoryHistory:all")
    @Log(title = "库存记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody InventoryHistoryDto bo) {
        inventoryHistoryService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除库存记录
     *
     * @param ids 主键串
     */
    @Operation(summary = "删除库存记录")
    @SaCheckPermission("wms:inventoryHistory:all")
    @Log(title = "库存记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        inventoryHistoryService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
