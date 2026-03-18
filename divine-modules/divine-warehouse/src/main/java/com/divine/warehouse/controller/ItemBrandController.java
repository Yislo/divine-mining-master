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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物品品牌
 *
 * @author yisl
 * @date 2024-07-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/itemBrand")
public class ItemBrandController extends BaseController {

    private final ItemBrandService itemBrandService;

    /**
     * 查询物品品牌列表
     */
    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/list")
    public PageInfoRes<ItemBrandVo> list(ItemBrandDto dto, BasePage basePage) {
        return itemBrandService.queryPageList(dto, basePage);
    }

    /**
     * 查询物品品牌列表
     */
//    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/listNoPage")
    public Result<List<ItemBrandVo>> listNoPage(ItemBrandDto dto) {
        return Result.success(itemBrandService.queryList(dto));
    }

    /**
     * 导出物品品牌列表
     */
    @SaCheckPermission("wms:itemBrand:list")
    @Log(title = "物品品牌", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ItemBrandDto dto, HttpServletResponse response) {
        List<ItemBrandVo> list = itemBrandService.queryList(dto);
        ExcelUtil.exportExcel(list, "物品品牌", ItemBrandVo.class, response);
    }

    /**
     * 获取物品品牌详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:itemBrand:list")
    @GetMapping("/{id}")
    public Result<ItemBrandVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(itemBrandService.queryById(id));
    }

    /**
     * 新增物品品牌
     */
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "物品品牌", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody ItemBrandDto dto) {
        itemBrandService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改物品品牌
     */
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "物品品牌", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody ItemBrandDto dto) {
        itemBrandService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除物品品牌
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:itemBrand:edit")
    @Log(title = "物品品牌", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> remove(@NotNull(message = "主键不能为空")
                          @PathVariable Long id) {
        itemBrandService.deleteById(id);
        return Result.success();
    }
}
