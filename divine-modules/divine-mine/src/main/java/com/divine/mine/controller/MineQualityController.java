package com.divine.mine.controller;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineQualityDto;
import com.divine.mine.domain.dto.QualityPageDTO;
import com.divine.mine.domain.vo.MineQualityInfoVo;
import com.divine.mine.domain.vo.MineQualityVo;
import com.divine.mine.service.MineQualityService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.divine.common.log.annotation.Log;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.log.enums.BusinessType;

/**
 * 送货质量
 *
 * @author yisl
 * @date 2026-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/quality")
public class MineQualityController extends BaseController {

    private final MineQualityService mineQualityService;

    /**
     * 新增质量单
     */
//    @SaCheckPermission("quality:add")
    @Log(title = "送货质量", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MineQualityDto dto) {
        dto.setQualityStatus(1);
        mineQualityService.insertByDto(dto);
        return Result.success();
    }

    /**
     * 暂存质量单
     */
//    @SaCheckPermission("quality:add")
    @Log(title = "送货质量", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/ts")
    public Result<Void> ts(@Validated(AddGroup.class) @RequestBody MineQualityDto dto) {
        dto.setQualityStatus(0);
        mineQualityService.insertByDto(dto);
        return Result.success();
    }

    /**
     * 查询质量单列表
     */
//    @SaCheckPermission("quality:list")
    @PostMapping("/list")
    public PageInfoRes<MineQualityVo> list(@RequestBody QualityPageDTO dto) {
        return mineQualityService.queryPageList(dto);
    }

    /**
     * 导出质量单
     */
//    @SaCheckPermission("quality:export")
    @Log(title = "送货质量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MineQualityDto dto, HttpServletResponse response) {
        List<MineQualityVo> list = mineQualityService.queryList(dto);
        ExcelUtil.exportExcel(list, "送货质量", MineQualityVo.class, response);
    }

    /**
     * 获取质量单详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("quality:query")
    @GetMapping("/{id}")
    public Result<MineQualityInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(mineQualityService.queryById(id));
    }


    /**
     * 修改质量单
     */
    @SaCheckPermission("quality:edit")
    @Log(title = "送货质量", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MineQualityDto dto) {
        mineQualityService.updateByBo(dto);
        return Result.success();
    }


    /**
     * 作废质量单
     */
    @SaCheckPermission("quality:edit")
    @Log(title = "送货质量", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/invalid/{id}")
    public Result<Void> invalid(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        mineQualityService.invalid(id);
        return Result.success();
    }

    /**
     * 删除送货质量
     *
     * @param ids 主键串
     */
    @SaCheckPermission("quality:remove")
    @Log(title = "送货质量", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        mineQualityService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
