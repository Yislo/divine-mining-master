package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.MovementOrderDto;
import com.divine.common.core.constant.ServiceConstants;
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
import com.divine.warehouse.domain.vo.MovementOrderVo;
import com.divine.warehouse.service.MovementOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 移库单
 *
 * @author zcc
 * @date 2024-08-09
 */
@Tag(name = "移库单")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/movementOrder")
public class MovementOrderController extends BaseController {

    private final MovementOrderService movementOrderService;

    /**
     * 查询移库单列表
     */
    @Operation(summary = "查询移库单列表")
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list")
    public PageInfoRes<MovementOrderVo> list(MovementOrderDto bo, BasePage basePage) {
        return movementOrderService.queryPageList(bo, basePage);
    }

    /**
     * 导出移库单列表
     */
    @Operation(summary = "导出移库单列表")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MovementOrderDto bo, HttpServletResponse response) {
        List<MovementOrderVo> list = movementOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "移库单", MovementOrderVo.class, response);
    }

    /**
     * 获取移库单详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取移库单详细信息")
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/{id}")
    public Result<MovementOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(movementOrderService.queryById(id));
    }

    /**
     * 新增移库单
     */
    @Operation(summary = "新增移库单")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MovementOrderDto bo) {
        bo.setOrderStatus(ServiceConstants.MovementOrderStatus.PENDING);
        movementOrderService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改移库单
     */
    @Operation(summary = "修改移库单")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MovementOrderDto bo) {
        movementOrderService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 移库
     */
    @Operation(summary = "移库")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/move")
    public Result<Void> move(@Validated(AddGroup.class) @RequestBody MovementOrderDto bo) {
        bo.setOrderStatus(ServiceConstants.MovementOrderStatus.FINISH);
        movementOrderService.move(bo);
        return Result.success();
    }

    /**
     * 删除移库单
     *
     * @param id 主键
     */
    @Operation(summary = "删除移库单")
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        movementOrderService.deleteById(id);
        return Result.success();
    }
}
