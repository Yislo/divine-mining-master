package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.ShipmentOrderDto;
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
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.warehouse.service.ShipmentOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库单
 *
 * @author zcc
 * @date 2024-08-01
 */
@Tag(name = "出库单")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/shipmentOrder")
public class ShipmentOrderController extends BaseController {

    private final ShipmentOrderService shipmentOrderService;

    /**
     * 查询出库单列表
     */
    @Operation(summary = "查询出库单列表")
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list")
    public PageInfoRes<ShipmentOrderVo> list(ShipmentOrderDto bo, BasePage basePage) {
        return shipmentOrderService.queryPageList(bo, basePage);
    }

    /**
     * 导出出库单列表
     */
    @Operation(summary = "导出出库单列表")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ShipmentOrderDto bo, HttpServletResponse response) {
        List<ShipmentOrderVo> list = shipmentOrderService.queryList(bo);
        ExcelUtil.exportExcel(list, "出库单", ShipmentOrderVo.class, response);
    }

    /**
     * 获取出库单详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取出库单详细信息")
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/{id}")
    public Result<ShipmentOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(shipmentOrderService.queryById(id));
    }

    @Operation(summary = "getIdByNo")
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/getIdByNo")
    public Result<Long> getId(@RequestParam String orderNo) {
        return Result.success(shipmentOrderService.queryIdByOrderNo(orderNo));
    }

    /**
     * 新增出库单
     */
    @Operation(summary = "新增出库单")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Long> add(@Validated(AddGroup.class) @RequestBody ShipmentOrderDto bo) {
        Long id = shipmentOrderService.insertByBo(bo);
        return Result.success(id);
    }

    /**
     * 修改出库单
     */
    @Operation(summary = "修改出库单")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ShipmentOrderDto bo) {
        shipmentOrderService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 出库
     */
    @Operation(summary = "出库")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/shipment")
    public Result<Void> shipment(@Validated(AddGroup.class) @RequestBody ShipmentOrderDto bo) {
        bo.setOrderStatus(ServiceConstants.ShipmentOrderStatus.FINISH);
        shipmentOrderService.shipment(bo);
        return Result.success();
    }

    /**
     * 删除出库单
     *
     * @param id 主键
     */
    @Operation(summary = "删除出库单")
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        shipmentOrderService.deleteById(id);
        return Result.success();
    }
}
