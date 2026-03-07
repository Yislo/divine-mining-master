package com.divine.warehouse.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.InventoryDto;
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
import com.divine.warehouse.domain.vo.BoardListVO;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.divine.warehouse.service.InventoryService;

import java.util.List;

/**
 * 库存
 *
 * @author yisl
 * @date 2024-07-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController extends BaseController {

    private final InventoryService inventoryService;

    /**
     * 查询库存列表物品维度
     * @param dto
     * @param basePage
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @GetMapping(value = {"/boardList/item"})
    public PageInfoRes<BoardListVO> queryItemBoardList(InventoryDto dto, BasePage basePage) {
        return inventoryService.queryItemBoardList(dto, basePage);
    }

    /**
     * 查询库存列表仓库维度
     * @param dto
     * @param basePage
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @GetMapping("/boardList/warehouse")
    public PageInfoRes<BoardListVO> queryWarehouseBoardList(InventoryDto dto, BasePage basePage) {
        return inventoryService.queryWarehouseBoardList(dto, basePage);
    }

    /**
     * 查询库存列表物品维度
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @GetMapping(value = {"/listNoPage"})
    public Result<List<InventoryVo>> listNoPage(InventoryDto dto) {
        return Result.success(inventoryService.queryList(dto));
    }

    /**
     * 查询可出库的物品
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @GetMapping(value = {"/boardList/choose"})
    public PageInfoRes<InventoryVo> choose(InventoryDto dto, BasePage basePage) {
        return inventoryService.queryChooseList(dto,basePage);
    }

    /**
     * 导出库存列表
     * @param dto
     * @param response
     */
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(InventoryDto dto, HttpServletResponse response) {
        List<InventoryVo> list = inventoryService.queryList(dto);
        ExcelUtil.exportExcel(list, "库存", InventoryVo.class, response);
    }

    /**
     * 获取库存详细信息
     * @param id
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @GetMapping("/{id}")
    public Result<InventoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(inventoryService.queryById(id));
    }

    /**
     * 新增库存
      * @param dto
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody InventoryDto dto) {
        inventoryService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改库存
     * @param dto
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody InventoryDto dto) {
        inventoryService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除库存
     * @param ids
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        inventoryService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 删除货架
     * @param id
     * @return
     */
    @SaCheckPermission("wms:inventory:all")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteStorageShelf/{id}")
    public Result<Void> deleteStorageShelf(@NotNull(message = "id不能为空") @PathVariable Long id) {
        inventoryService.deleteStorageShelf(id);
        return Result.success();
    }
}
