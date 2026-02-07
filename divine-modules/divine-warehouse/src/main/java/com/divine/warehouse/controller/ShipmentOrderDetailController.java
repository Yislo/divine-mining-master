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
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVo;
import com.divine.warehouse.service.ShipmentOrderDetailService;
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
 * 出库单详情
 *
 * @author zcc
 * @date 2024-08-01
 */
@Tag(name = "出库单详情")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/shipmentOrderDetail")
public class ShipmentOrderDetailController extends BaseController {

    private final ShipmentOrderDetailService shipmentOrderDetailService;

    /**
     * 查询出库单详情列表
     */
    @Operation(summary = "查询出库单详情列表")
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list")
    public PageInfoRes<ShipmentOrderDetailVo> list(ShipmentOrderDetailDto bo, BasePage basePage) {
        return shipmentOrderDetailService.queryPageList(bo, basePage);
    }

    /**
     * 导出出库单详情列表
     */
    @Operation(summary = "导出出库单详情列表")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ShipmentOrderDetailDto bo, HttpServletResponse response) {
        List<ShipmentOrderDetailVo> list = shipmentOrderDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "出库单详情", ShipmentOrderDetailVo.class, response);
    }

    /**
     * 获取出库单详情详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取出库单详情详细信息")
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/{id}")
    public Result<ShipmentOrderDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(shipmentOrderDetailService.queryById(id));
    }

    /**
     * 新增出库单详情
     */
    @Operation(summary = "新增出库单详情")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ShipmentOrderDetailDto bo) {
        shipmentOrderDetailService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改出库单详情
     */
    @Operation(summary = "修改出库单详情")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ShipmentOrderDetailDto bo) {
        shipmentOrderDetailService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除出库单详情
     *
     * @param ids 主键串
     */
    @Operation(summary = "删除出库单详情")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        shipmentOrderDetailService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 获取出库单详情详细信息
     *
     */
    @Operation(summary = "获取出库单详情详细信息")
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list/{shipmentOrderId}")
    public Result<List<ShipmentOrderDetailVo>> listByShipmentOrderId(@NotNull @PathVariable Long shipmentOrderId) {
        return Result.success(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderId));
    }
}
