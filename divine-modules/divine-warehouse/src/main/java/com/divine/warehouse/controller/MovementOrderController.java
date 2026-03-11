package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.warehouse.domain.dto.MovementOrderDto;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 移库单
 *
 * @author yisl
 * @date 2024-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/movementOrder")
public class MovementOrderController extends BaseController {

    private final MovementOrderService movementOrderService;

    /**
     * 查询移库单列表
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list")
    public PageInfoRes<MovementOrderVo> list(MovementOrderDto dto, BasePage basePage) {
        return movementOrderService.queryPageList(dto, basePage);
    }

    /**
     * 导出移库单列表
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MovementOrderDto dto, HttpServletResponse response) {
        List<MovementOrderVo> list = movementOrderService.queryList(dto);
        ExcelUtil.exportExcel(list, "移库单", MovementOrderVo.class, response);
    }

    /**
     * 获取移库单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/{id}")
    public Result<MovementOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(movementOrderService.queryById(id));
    }

    /**
     * 新增移库单
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MovementOrderDto dto) {
        dto.setMoveStatus(InventoryStatusEnum.PENDING.getCode());
        movementOrderService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改移库单
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MovementOrderDto dto) {
        movementOrderService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 移库
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/move")
    public Result<Void> move(@Validated(AddGroup.class) @RequestBody MovementOrderDto dto) {
        movementOrderService.move(dto);
        return Result.success();
    }

    /**
     * 删除移库单
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "移库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        movementOrderService.deleteById(id);
        return Result.success();
    }
}
