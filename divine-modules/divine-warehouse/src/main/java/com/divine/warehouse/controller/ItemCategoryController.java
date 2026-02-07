package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.warehouse.domain.dto.ItemCategoryDto;
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
import com.divine.warehouse.domain.vo.ItemCategoryVo;
import com.divine.warehouse.domain.vo.ItemTypeTreeSelectVo;
import com.divine.warehouse.service.ItemCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Tag(name = "物品类型")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/itemCategory")
public class ItemCategoryController extends BaseController {

    private final ItemCategoryService itemCategoryService;

    /**
     * 查询物料类型列表
     */
    @Operation(summary = "查询物料类型列表")
    @GetMapping("/list")
    @SaCheckPermission("wms:item:list")
    public PageInfoRes<ItemCategoryVo> list(ItemCategoryDto bo, BasePage basePage) {
        return itemCategoryService.queryPageList(bo, basePage);
    }

    /**
     * 查询物料类型列表
     */
    @Operation(summary = "查询物料类型列表")
    @GetMapping("/listNoPage")
    @SaCheckPermission("wms:item:list")
    public Result<List<ItemCategoryVo>> listNoPage(ItemCategoryDto bo) {
        return Result.success(itemCategoryService.queryList(bo));
    }

    /**
     * 获取物料类型下拉树列表
     */
    @Operation(summary = "获取物料类型下拉树列表")
    @GetMapping("/treeselect")
    @SaCheckPermission("wms:item:list")
    public Result<List<ItemTypeTreeSelectVo>> treeselect(ItemCategoryDto query) {
        List<ItemCategoryVo> itemTypes = itemCategoryService.queryList(query);
        return Result.success(itemCategoryService.buildItemTypeTreeSelect(itemTypes));
    }

    /**
     * 导出物料类型列表
     */
    @Operation(summary = "导出物料类型列表")
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:item:list")
    public void export(ItemCategoryDto bo, HttpServletResponse response) {
        List<ItemCategoryVo> list = itemCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "物料类型", ItemCategoryVo.class, response);
    }

    /**
     * 获取物料类型详细信息
     *
     * @param itemTypeId 主键
     */
    @Operation(summary = "获取物料类型详细信息")
    @GetMapping("/{itemTypeId}")
    @SaCheckPermission("wms:item:list")
    public Result<ItemCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long itemTypeId) {
        return Result.success(itemCategoryService.queryById(itemTypeId));
    }

    /**
     * 新增物料类型
     */
    @Operation(summary = "新增物料类型")
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemCategoryDto bo) {
        itemCategoryService.insertByBo(bo);
        return Result.success();
    }

    /**
     * 修改物料类型
     */
    @Operation(summary = "修改物料类型")
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemCategoryDto bo) {
        itemCategoryService.updateByBo(bo);
        return Result.success();
    }

    /**
     * 删除物料类型
     *
     * @param itemTypeIds 主键串
     */
    @Operation(summary = "删除物料类型")
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemTypeIds}")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] itemTypeIds) {
        List<Long> ids = new ArrayList<>(Arrays.asList(itemTypeIds));
        itemCategoryService.deleteByIds(ids);
        return Result.success();
    }

    @Operation(summary = "修改")
    @PostMapping("/update/orderNum")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> updateOrderNum(@RequestBody List<ItemTypeTreeSelectVo> tree) {
        itemCategoryService.updateOrderNum(tree);
        return Result.success();
    }
}
