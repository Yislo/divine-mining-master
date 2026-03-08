package com.divine.mine.controller;

import java.util.List;

import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineWeightingDto;
import com.divine.mine.domain.vo.MineWeightingVo;
import com.divine.mine.service.MineWeightingService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.divine.common.log.annotation.Log;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.log.enums.BusinessType;

/**
 * 过磅记录
 *
 * @author yisl
 * @date 2026-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/weighting")
public class MineWeightingController extends BaseController {

    private final MineWeightingService mineWeightingService;

    /**
     * 查询过磅记录列表
     */
//    @SaCheckPermission("weighting:list")
    @GetMapping("/list")
    public PageInfoRes<MineWeightingVo> list(MineWeightingDto dto, BasePage basePage) {
        return mineWeightingService.queryPageList(dto, basePage);
    }

    /**
     * 导出过磅记录列表
     */
//    @SaCheckPermission("weighting:export")
    @Log(title = "过磅记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MineWeightingDto dto, HttpServletResponse response) {
        List<MineWeightingVo> list = mineWeightingService.queryList(dto);
        ExcelUtil.exportExcel(list, "过磅记录", MineWeightingVo.class, response);
    }

    /**
     * 获取过磅记录详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("weighting:query")
    @GetMapping("/{id}")
    public Result<MineWeightingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(mineWeightingService.queryById(id));
    }

    /**
     * 新增过磅记录
     */
//    @SaCheckPermission("weighting:add")
    @Log(title = "过磅记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MineWeightingDto dto) {
        mineWeightingService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改过磅记录
     */
//    @SaCheckPermission("weighting:edit")
    @Log(title = "过磅记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MineWeightingDto dto) {
        mineWeightingService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除过磅记录
     *
     * @param ids 主键串
     */
//    @SaCheckPermission("weighting:remove")
    @Log(title = "过磅记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        mineWeightingService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
