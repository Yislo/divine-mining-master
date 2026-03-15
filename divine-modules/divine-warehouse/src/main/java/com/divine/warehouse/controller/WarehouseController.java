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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/warehouse")
public class WarehouseController extends BaseController {

    private final WarehouseService warehouseService;

    /**
     * 查询仓库列表
     */
    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/list")
    public PageInfoRes<WarehouseVo> list(WarehouseDto dto, BasePage basePage) {
        return warehouseService.queryPageList(dto, basePage);
    }

    /**
     * 查询仓库列表
     */
    @GetMapping("/listNoPage")
    public Result<List<WarehouseVo>> listNoPage(WarehouseDto dto) {
        return Result.success(warehouseService.queryList(dto));
    }

    /**
     * 获取仓库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:warehouse:list")
    @GetMapping("/{id}")
    public Result<WarehouseVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return Result.success(warehouseService.queryById(id));
    }

    /**
     * 新增仓库
     */
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody WarehouseDto dto) {
        warehouseService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改仓库
     */
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody WarehouseDto dto) {
        warehouseService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除仓库
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        warehouseService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改排序
     * @param tree
     * @return
     */
    @SaCheckPermission("wms:warehouse:edit")
    @Log(title = "仓库", businessType = BusinessType.UPDATE)
    @PostMapping("/update/sort")
    public Result<Void> updateSort(@RequestBody List<WarehouseDto> tree) {
        warehouseService.updateSort(tree);
        return Result.success();
    }
}
