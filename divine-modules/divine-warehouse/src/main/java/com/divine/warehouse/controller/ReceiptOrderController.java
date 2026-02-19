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
import com.divine.warehouse.domain.dto.ReceiptOrderDto;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.ReceiptOrderService;

import java.util.List;

/**
 * 入库单
 *
 * @author yisl
 * @date 2024-07-19
 */
@Tag(name = "入库单")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/receiptOrder")
public class ReceiptOrderController extends BaseController {

    private final ReceiptOrderService receiptOrderService;

    @Operation(summary = "查询入库单列表")
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/list")
    public PageInfoRes<ReceiptOrderVo> list(ReceiptOrderDto dto, BasePage basePage) {
        return receiptOrderService.queryPageList(dto, basePage);
    }

    @Operation(summary = "导出入库单列表")
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ReceiptOrderDto dto, HttpServletResponse response) {
        List<ReceiptOrderVo> list = receiptOrderService.queryList(dto);
        ExcelUtil.exportExcel(list, "入库单", ReceiptOrderVo.class, response);
    }

    @Operation(summary = "获取入库单详细信息")
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/{id}")
    public Result<ReceiptOrderVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(receiptOrderService.queryById(id));
    }

    @Operation(summary = "获取id")
    @SaCheckPermission("wms:receipt:all")
    @GetMapping("/getIdByNo")
    public Result<Long> getId(@RequestParam String orderNo) {
        return Result.success(receiptOrderService.queryIdByOrderNo(orderNo));
    }

    @Operation(summary = "暂存入库单")
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Long> add(@Validated(AddGroup.class) @RequestBody ReceiptOrderDto dto) {
        return Result.success(receiptOrderService.insertByBo(dto));
    }

    @Operation(summary = "入库")
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/warehousing")
    public Result<Void> warehousing(@Validated(AddGroup.class) @RequestBody ReceiptOrderDto dto) {
        receiptOrderService.warehousing(dto);
        return Result.success();
    }

    @Operation(summary = "修改入库单")
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ReceiptOrderDto dto) {
        receiptOrderService.updateByBo(dto);
        return Result.success();
    }

    @Operation(summary = "删除入库单")
    @SaCheckPermission("wms:receipt:all")
    @Log(title = "入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        receiptOrderService.deleteById(id);
        return Result.success();
    }
}
