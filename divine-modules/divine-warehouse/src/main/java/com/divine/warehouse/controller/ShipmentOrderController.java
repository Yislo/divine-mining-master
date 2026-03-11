package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.warehouse.domain.dto.ShipmentOrderDto;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库单
 *
 * @author yisl
 * @date 2024-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/shipmentOrder")
public class ShipmentOrderController extends BaseController {

    private final ShipmentOrderService shipmentOrderService;

    /**
     * 查询出库单列表
     */
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list")
    public PageInfoRes<ShipmentOrderVo> list(ShipmentOrderDto dto, BasePage basePage) {
        return shipmentOrderService.queryPageList(dto, basePage);
    }

    /**
     * 导出出库单列表
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ShipmentOrderDto dto, HttpServletResponse response) {
        List<ShipmentOrderVo> list = shipmentOrderService.queryList(dto);
        ExcelUtil.exportExcel(list, "出库单", ShipmentOrderVo.class, response);
    }

    /**
     * 获取出库单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/{id}")
    public Result<ShipmentOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(shipmentOrderService.queryById(id));
    }

    /**
     * 获取id
     * @param orderNo
     * @return
     */
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/getIdByNo")
    public Result<Long> getId(@RequestParam String orderNo) {
        return Result.success(shipmentOrderService.queryIdByOrderNo(orderNo));
    }

    /**
     * 新增出库单
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Long> add(@Validated(AddGroup.class) @RequestBody ShipmentOrderDto dto) {
        dto.setShipmentStatus(InventoryStatusEnum.PENDING.getCode());
        Long id = shipmentOrderService.insertByBo(dto);
        return Result.success(id);
    }

    /**
     * 修改出库单
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ShipmentOrderDto dto) {
        shipmentOrderService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 出库
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/shipment")
    public Result<Void> shipment(@Validated(AddGroup.class) @RequestBody ShipmentOrderDto dto) {
        shipmentOrderService.shipment(dto);
        return Result.success();
    }

    /**
     * 删除出库单
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        shipmentOrderService.deleteById(id);
        return Result.success();
    }
}
