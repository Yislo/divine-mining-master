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
import com.divine.warehouse.domain.vo.MoveDetailVO;
import com.divine.warehouse.domain.vo.MovementOrderDetailVO;
import com.divine.warehouse.service.MovementOrderDetailService;
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
 * @author yisl
 * @date 2024-08-09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/movementOrderDetail")
public class MovementOrderDetailController extends BaseController {

    private final MovementOrderDetailService movementOrderDetailService;

    /**
     * 查询库存移动详情列表
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list")
    public PageInfoRes<MoveDetailVO> list(Long moveId, BasePage basePage) {
        return movementOrderDetailService.queryPageList(moveId, basePage);
    }

    /**
     * 获取库存移动详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/{id}")
    public Result<MovementOrderDetailVO> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(movementOrderDetailService.queryById(id));
    }

    /**
     * 新增库存移动详情
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MovementOrderDetailDto dto) {
        movementOrderDetailService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改库存移动详情
     */
    @SaCheckPermission("wms:movement:all")
    @Log(title = "库存移动详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MovementOrderDetailDto dto) {
        movementOrderDetailService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除库存移动详情
     *
     * @param ids 主键串
     */
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
    @SaCheckPermission("wms:movement:all")
    @GetMapping("/list/{movementOrderId}")
    public Result<List<MovementOrderDetailVO>> listByMovementOrderId(@NotNull @PathVariable Long movementOrderId) {
        return Result.success(movementOrderDetailService.queryByMovementOrderId(movementOrderId));
    }
}
