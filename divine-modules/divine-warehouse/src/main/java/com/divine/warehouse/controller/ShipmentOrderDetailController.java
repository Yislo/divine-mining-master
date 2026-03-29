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
import com.divine.warehouse.domain.dto.ShipmentInfoDto;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.vo.ShipmentDetailVO;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVO;
import com.divine.warehouse.service.ShipmentOrderDetailService;
import com.dtflys.forest.annotation.Post;
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
 * @author yisl
 * @date 2024-08-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/shipmentOrderDetail")
public class ShipmentOrderDetailController extends BaseController {

    private final ShipmentOrderDetailService shipmentOrderDetailService;

    /**
     * 查询出库单详情列表
     */
    @SaCheckPermission("wms:shipment:all")
    @PostMapping("/list")
    public PageInfoRes<ShipmentDetailVO> list(@RequestBody ShipmentInfoDto dto) {
        return shipmentOrderDetailService.queryPageList(dto);
    }

    /**
     * 获取出库单详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/{id}")
    public Result<ShipmentOrderDetailVO> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(shipmentOrderDetailService.queryById(id));
    }

    /**
     * 新增出库单详情
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ShipmentOrderDetailDto dto) {
        shipmentOrderDetailService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改出库单详情
     */
    @SaCheckPermission("wms:shipment:all")
    @Log(title = "出库单详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ShipmentOrderDetailDto dto) {
        shipmentOrderDetailService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除出库单详情
     *
     * @param ids 主键串
     */
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
    @SaCheckPermission("wms:shipment:all")
    @GetMapping("/list/{shipmentOrderId}")
    public Result<List<ShipmentOrderDetailVO>> listByShipmentOrderId(@NotNull @PathVariable Long shipmentOrderId) {
        return Result.success(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderId));
    }
}
