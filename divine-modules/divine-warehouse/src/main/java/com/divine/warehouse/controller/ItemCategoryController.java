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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 物品类型
 *
 * @author yisl
 * @date 2024-07-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/itemCategory")
public class ItemCategoryController extends BaseController {

    private final ItemCategoryService itemCategoryService;

    /**
     * 查询物料类型列表
     */
    @GetMapping("/list")
    @SaCheckPermission("wms:item:list")
    public PageInfoRes<ItemCategoryVo> list(ItemCategoryDto dto, BasePage basePage) {
        return itemCategoryService.queryPageList(dto, basePage);
    }

    /**
     * 查询物料类型列表
     */
    @GetMapping("/listNoPage")
//    @SaCheckPermission("wms:item:list")
    public Result<List<ItemCategoryVo>> listNoPage(ItemCategoryDto dto) {
        return Result.success(itemCategoryService.queryList(dto));
    }

    /**
     * 获取物料类型下拉树列表
     */
    @GetMapping("/treeselect")
//    @SaCheckPermission("wms:item:list")
    public Result<List<ItemTypeTreeSelectVo>> treeselect(ItemCategoryDto query) {
        List<ItemCategoryVo> itemTypes = itemCategoryService.queryList(query);
        return Result.success(itemCategoryService.buildItemTypeTreeSelect(itemTypes));
    }

    /**
     * 导出物料类型列表
     */
    @Log(title = "物料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @SaCheckPermission("wms:item:list")
    public void export(ItemCategoryDto dto, HttpServletResponse response) {
        List<ItemCategoryVo> list = itemCategoryService.queryList(dto);
        ExcelUtil.exportExcel(list, "物料类型", ItemCategoryVo.class, response);
    }

    /**
     * 获取物料类型详细信息
     *
     * @param itemTypeId 主键
     */
    @GetMapping("/{itemTypeId}")
    @SaCheckPermission("wms:item:list")
    public Result<ItemCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long itemTypeId) {
        return Result.success(itemCategoryService.queryById(itemTypeId));
    }

    /**
     * 新增物料类型
     */
    @Log(title = "物料类型", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
    @PostMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemCategoryDto dto) {
        itemCategoryService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改物料类型
     */
    @Log(title = "物料类型", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
    @PutMapping()
    @SaCheckPermission("wms:item:edit")
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemCategoryDto dto) {
        itemCategoryService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除物料类型
     *
     * @param itemTypeIds 主键串
     */
    @Log(title = "物料类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{itemTypeIds}")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] itemTypeIds) {
        List<Long> ids = new ArrayList<>(Arrays.asList(itemTypeIds));
        itemCategoryService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 修改
     * @param tree
     * @return
     */
    @PostMapping("/update/sort")
    @SaCheckPermission("wms:item:edit")
    public Result<Void> updateSort(@RequestBody List<ItemTypeTreeSelectVo> tree) {
        itemCategoryService.updateSort(tree);
        return Result.success();
    }
}
