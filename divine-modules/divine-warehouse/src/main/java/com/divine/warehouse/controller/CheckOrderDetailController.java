package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
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
import com.divine.warehouse.domain.vo.CheckOrderDetailVo;
import com.divine.warehouse.service.CheckOrderDetailService;
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
 * 库存盘点单据详情
 *
 * @author zcc
 * @date 2024-08-13
 */
@Tag(name = "库存盘点单据详情")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkOrderDetail")
public class CheckOrderDetailController extends BaseController {

    private final CheckOrderDetailService checkOrderDetailService;

    /**
     * 查询库存盘点单据详情列表
     */
    @Operation(summary = "查询库存盘点单据详情列表")
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list")
    public PageInfoRes<CheckOrderDetailVo> list(CheckOrderDetailDto bo, BasePage basePage) {
        return checkOrderDetailService.queryPageList(bo, basePage);
    }

    /**
     * 导出库存盘点单据详情列表
     */
    @Operation(summary = "导出库存盘点单据详情列表")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CheckOrderDetailDto bo, HttpServletResponse response) {
        List<CheckOrderDetailVo> list = checkOrderDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存盘点单据详情", CheckOrderDetailVo.class, response);
    }

    /**
     * 获取库存盘点单据详情详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取库存盘点单据详情详细信息")
    @SaCheckPermission("wms:check:all")
    @GetMapping("/{id}")
    public Result<CheckOrderDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(checkOrderDetailService.queryById(id));
    }

    /**
     * 新增库存盘点单据详情
     */
    @Operation(summary = "新增库存盘点单据详情")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody CheckOrderDetailDto bo) {
        checkOrderDetailService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改库存盘点单据详情
     */
    @Operation(summary = "修改库存盘点单据详情")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody CheckOrderDetailDto bo) {
        checkOrderDetailService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除库存盘点单据详情
     *
     * @param ids 主键串
     */
    @Operation(summary = "删除库存盘点单据详情")
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        checkOrderDetailService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 根据盘库单id查询盘库单详情列表
     */
    @Operation(summary = "根据盘库单id查询盘库单详情列表")
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list/{checkOrderId}")
    public Result<List<CheckOrderDetailVo>> listByCheckOrderId(@NotNull @PathVariable Long checkOrderId) {
        return Result.success(checkOrderDetailService.queryByCheckOrderId(checkOrderId));
    }
}
