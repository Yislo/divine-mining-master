package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.WarehouseDto;
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
import com.divine.warehouse.domain.vo.WarehouseVo;
import com.divine.warehouse.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "仓库")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController {

    private final WarehouseService warehouseService;

    /**
     * 查询仓库列表
     */
    @Operation(summary = "查询仓库列表")
    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/list")
    public PageInfoRes<WarehouseVo> list(WarehouseDto bo, BasePage basePage) {
        return warehouseService.queryPageList(bo, basePage);
    }

    /**
     * 查询仓库列表
     */
    @Operation(summary = "查询仓库列表")
    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/listNoPage")
    public Result<List<WarehouseVo>> listNoPage(WarehouseDto bo) {
        return Result.success(warehouseService.queryList(bo));
    }

    /**
     * 导出仓库列表
     */
    @Operation(summary = "导出仓库列表")
    @SaCheckPermission("wms:warehouse:list")
    @Log(title = "仓库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WarehouseDto bo, HttpServletResponse response) {
        List<WarehouseVo> list = warehouseService.queryList(bo);
        ExcelUtil.exportExcel(list, "仓库", WarehouseVo.class, response);
    }

    /**
     * 获取仓库详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取仓库详细信息")
    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/{id}")
    public Result<WarehouseVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return Result.success(warehouseService.queryById(id));
    }

    /**
     * 新增仓库
     */
    @Operation(summary = "新增仓库")
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody WarehouseDto bo) {
        warehouseService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改仓库
     */
    @Operation(summary = "修改仓库")
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody WarehouseDto bo) {
        warehouseService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除仓库
     *
     * @param id 主键
     */
    @Operation(summary = "删除仓库")
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        warehouseService.deleteById(id);
        return Result.success();
    }

    @Operation(summary = "/update/orderNum")
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PostMapping("/update/orderNum")
    public Result<Void> updateOrderNum(@RequestBody List<WarehouseDto> tree) {
        warehouseService.updateOrderNum(tree);
        return Result.success();
    }
}
