package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation .SaCheckPermission;
import com.divine.warehouse.domain.dto.CheckOrderDto;
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
import com.divine.warehouse.domain.vo.CheckOrderVo;
import com.divine.warehouse.service.CheckOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存盘点单据
 *
 * @author zcc
 * @date 2024-08-13
 */
@Tag(name = "库存盘点单据")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkOrder")
public class CheckOrderController extends BaseController {

    private final CheckOrderService checkOrderService;

    /**
     * 查询库存盘点单据列表
     */
    @Operation(summary = "查询库存盘点单据列表")
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list")
    public PageInfoRes<CheckOrderVo> list(CheckOrderDto dto, BasePage basePage) {
        return checkOrderService.queryPageList(dto, basePage);
    }

    /**
     * 导出库存盘点单据列表
     */
    @Operation(summary = "导出库存盘点单据列表")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CheckOrderDto bo, HttpServletResponse response) {
        List<CheckOrderVo> list = checkOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存盘点单据", CheckOrderVo.class, response);
    }

    /**
     * 获取库存盘点单据详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取库存盘点单据详细信息")
    @SaCheckPermission("wms:check:all")
    @GetMapping("/{id}")
    public Result<CheckOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(checkOrderService.queryById(id));
    }

    /**
     * 新增库存盘点单据
     */
    @Operation(summary = "新增库存盘点单据")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody CheckOrderDto bo) {
        bo.setOrderStatus(ServiceConstants.CheckOrderStatus.PENDING);
        checkOrderService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改库存盘点单据
     */
    @Operation(summary = "修改库存盘点单据")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody CheckOrderDto bo) {
        checkOrderService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 盘库结束
     */
    @Operation(summary = "盘库结束")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/check")
    public Result<Void> check(@Validated(AddGroup.class) @RequestBody CheckOrderDto bo) {
        bo.setOrderStatus(ServiceConstants.CheckOrderStatus.FINISH);
        checkOrderService.check(bo);
        return Result.success();
    }

    /**
     * 删除库存盘点单据
     *
     * @param id 主键
     */
    @Operation(summary = "删除库存盘点单据")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        checkOrderService.deleteById(id);
        return Result.success();
    }
}
