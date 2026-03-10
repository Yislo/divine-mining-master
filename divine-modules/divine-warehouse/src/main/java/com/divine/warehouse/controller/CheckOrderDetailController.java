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
import com.divine.warehouse.domain.vo.CheckOrderDetailVO;
import com.divine.warehouse.service.CheckOrderDetailService;
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
 * @author yisl
 * @date 2024-08-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/checkOrderDetail")
public class CheckOrderDetailController extends BaseController {

    private final CheckOrderDetailService checkOrderDetailService;

    /**
     * 查询库存盘点单据详情列表
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list")
    public PageInfoRes<CheckOrderDetailVO> list(CheckOrderDetailDto dto, BasePage basePage) {
        return checkOrderDetailService.queryPageList(dto, basePage);
    }

    /**
     * 获取库存盘点单据详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:check:all")
    @GetMapping("/{id}")
    public Result<CheckOrderDetailVO> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(checkOrderDetailService.queryById(id));
    }

    /**
     * 新增库存盘点单据详情
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody CheckOrderDetailDto dto) {
        checkOrderDetailService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改库存盘点单据详情
     */
    @SaCheckPermission("wms:check:all")
    @Log(title = "库存盘点单据详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody CheckOrderDetailDto dto) {
        checkOrderDetailService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除库存盘点单据详情
     *
     * @param ids 主键串
     */
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
    @SaCheckPermission("wms:check:all")
    @GetMapping("/list/{checkOrderId}")
    public Result<List<CheckOrderDetailVO>> listByCheckOrderId(@NotNull @PathVariable Long checkOrderId) {
        return Result.success(checkOrderDetailService.queryByCheckOrderId(checkOrderId));
    }
}
