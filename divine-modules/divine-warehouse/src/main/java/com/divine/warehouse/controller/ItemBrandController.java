package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.ItemBrandDto;
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
import com.divine.warehouse.domain.vo.ItemBrandVo;
import com.divine.warehouse.service.ItemBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品品牌
 *
 * @author zcc
 * @date 2024-07-30
 */
@Tag(name = "商品品牌")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/itemBrand")
public class ItemBrandController extends BaseController {

    private final ItemBrandService itemBrandService;

    /**
     * 查询商品品牌列表
     */
    @Operation(summary = "查询商品品牌列表")
    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/list")
    public PageInfoRes<ItemBrandVo> list(ItemBrandDto bo, BasePage basePage) {
        return itemBrandService.queryPageList(bo, basePage);
    }

    /**
     * 查询商品品牌列表
     */
    @Operation(summary = "查询商品品牌列表")
    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/listNoPage")
    public Result<List<ItemBrandVo>> listNoPage(ItemBrandDto bo) {
        return Result.success(itemBrandService.queryList(bo));
    }

    /**
     * 导出商品品牌列表
     */
    @Operation(summary = "导出商品品牌列表")
    @SaCheckPermission("wms:itemBrand:list")
    @Log(title = "商品品牌", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ItemBrandDto bo, HttpServletResponse response) {
        List<ItemBrandVo> list = itemBrandService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品品牌", ItemBrandVo.class, response);
    }

    /**
     * 获取商品品牌详细信息
     *
     * @param id 主键
     */
    @Operation(summary = "获取商品品牌详细信息")
    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/{id}")
    public Result<ItemBrandVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(itemBrandService.queryById(id));
    }

    /**
     * 新增商品品牌
     */
    @Operation(summary = "新增商品品牌")
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "商品品牌", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemBrandDto bo) {
        itemBrandService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改商品品牌
     */
    @Operation(summary = "修改商品品牌")
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "商品品牌", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemBrandDto bo) {
        itemBrandService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除商品品牌
     *
     * @param id 主键
     */
    @Operation(summary = "删除商品品牌")
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "商品品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        itemBrandService.deleteById(id);
        return Result.success();
    }
}
