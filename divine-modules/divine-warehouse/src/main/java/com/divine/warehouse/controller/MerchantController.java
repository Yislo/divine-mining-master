package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.MerchantDto;
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
import com.divine.warehouse.domain.vo.MerchantVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.MerchantService;

import java.util.List;

/**
 * 往来单位
 *
 * @author zcc
 * @date 2024-07-16
 */
@Tag(name = "往来单位")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant")
public class MerchantController extends BaseController {

    private final MerchantService merchantService;

    /**
     * 查询往来单位列表
     */
    @Operation(summary = "查询往来单位列表")
    @SaCheckPermission("wms:merchant:list")
    @GetMapping("/list")
    public PageInfoRes<MerchantVo> list(MerchantDto bo, BasePage basePage) {
        return merchantService.queryPageList(bo, basePage);
    }

    /**
     * 查询往来单位列表
     */
    @Operation(summary = "查询往来单位列表")
    @SaCheckPermission("wms:merchant:list")
    @GetMapping("/listNoPage")
    public Result<List<MerchantVo>> listNoPage(MerchantDto bo) {
        return Result.success(merchantService.queryList(bo));
    }

    /**
     * 导出往来单位列表
     */
    @Operation(summary = "导出往来单位列表")
    @SaCheckPermission("wms:merchant:list")
    @Log(title = "往来单位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MerchantDto bo, HttpServletResponse response) {
        List<MerchantVo> list = merchantService.queryList(bo);
        ExcelUtil.exportExcel(list, "往来单位", MerchantVo.class, response);
    }

    /**
     * 获取往来单位详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取往来单位详细信息")
    @SaCheckPermission("wms:merchant:list")
    @GetMapping("/{id}")
    public Result<MerchantVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(merchantService.queryById(id));
    }

    /**
     * 新增往来单位
     */
    @Operation(summary = "新增往来单位")
    @SaCheckPermission("wms:merchant:edit")
    @Log(title = "往来单位", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MerchantDto bo) {
        merchantService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改往来单位
     */
    @Operation(summary = "修改往来单位")
    @SaCheckPermission("wms:merchant:edit")
    @Log(title = "往来单位", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MerchantDto bo) {
        merchantService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除往来单位
     *
     * @param id 主键
     */
    @Operation(summary = "删除往来单位")
    @SaCheckPermission("wms:merchant:edit")
    @Log(title = "往来单位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        merchantService.deleteById(id);
        return Result.success();
    }
}
