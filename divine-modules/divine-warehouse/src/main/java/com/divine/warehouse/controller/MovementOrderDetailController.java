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
import com.divine.warehouse.domain.dto.MovementOrderDetailDto;
import com.divine.warehouse.domain.vo.MovementOrderDetailVo;
import com.divine.warehouse.service.MovementOrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存移动详情
 *
 * @author zcc
 * @date 2024-08-09
 */
@Tag(name = "库存移动详情")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/movementOrderDetail")
public class MovementOrderDetailController extends BaseController {

    private final MovementOrderDetailService movementOrderDetailService;

    /**
     * 查询库存移动详情列表
     */
    @Operation(summary = "查询库存移动详情列表")
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list")
    public PageInfoRes<MovementOrderDetailVo> list(MovementOrderDetailDto bo, BasePage basePage) {
        return movementOrderDetailService.queryPageList(bo, basePage);
    }

    /**
     * 导出库存移动详情列表
     */
    @Operation(summary = "导出库存移动详情列表")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MovementOrderDetailDto bo, HttpServletResponse response) {
        List<MovementOrderDetailVo> list = movementOrderDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存移动详情", MovementOrderDetailVo.class, response);
    }

    /**
     * 获取库存移动详情详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取库存移动详情详细信息")
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/{id}")
    public Result<MovementOrderDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(movementOrderDetailService.queryById(id));
    }

    /**
     * 新增库存移动详情
     */
    @Operation(summary = "新增库存移动详情")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MovementOrderDetailDto bo) {
        movementOrderDetailService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改库存移动详情
     */
    @Operation(summary = "修改库存移动详情")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MovementOrderDetailDto bo) {
        movementOrderDetailService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除库存移动详情
     *
     * @param ids 主键串
     */
    @Operation(summary = "删除库存移动详情")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        movementOrderDetailService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 根据移库单id查询移库单详情列表
     */
    @Operation(summary = "根据移库单id查询移库单详情列表")
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list/{movementOrderId}")
    public Result<List<MovementOrderDetailVo>> listByMovementOrderId(@NotNull @PathVariable Long movementOrderId) {
        return Result.success(movementOrderDetailService.queryByMovementOrderId(movementOrderId));
    }
}
