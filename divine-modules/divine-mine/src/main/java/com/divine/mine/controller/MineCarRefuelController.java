package com.divine.mine.controller;

import java.util.List;

import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineCarRefuelDto;
import com.divine.mine.domain.vo.MineCarRefuelVo;
import com.divine.mine.service.MineCarRefuelService;
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
 * 车辆加油记录
 *
 * @author yisl
 * @date 2026-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/carRefuel")
public class MineCarRefuelController extends BaseController {

    private final MineCarRefuelService mineCarRefuelService;

    /**
     * 查询车辆加油记录列表
     */
//    @SaCheckPermission("carRefuel:list")
    @GetMapping("/list")
    public PageInfoRes<MineCarRefuelVo> list(MineCarRefuelDto dto, BasePage basePage) {
        return mineCarRefuelService.queryPageList(dto, basePage);
    }

    /**
     * 导出车辆加油记录列表
     */
//    @SaCheckPermission("carRefuel:export")
    @Log(title = "车辆加油记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MineCarRefuelDto dto, HttpServletResponse response) {
        List<MineCarRefuelVo> list = mineCarRefuelService.queryList(dto);
        ExcelUtil.exportExcel(list, "车辆加油记录", MineCarRefuelVo.class, response);
    }

    /**
     * 获取车辆加油记录详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("carRefuel:query")
    @GetMapping("/{id}")
    public Result<MineCarRefuelVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(mineCarRefuelService.queryById(id));
    }

    /**
     * 新增车辆加油记录
     */
//    @SaCheckPermission("carRefuel:add")
    @Log(title = "车辆加油记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MineCarRefuelDto dto) {
        mineCarRefuelService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改车辆加油记录
     */
//    @SaCheckPermission("carRefuel:edit")
    @Log(title = "车辆加油记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MineCarRefuelDto dto) {
        mineCarRefuelService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除车辆加油记录
     *
     * @param ids 主键串
     */
//    @SaCheckPermission("carRefuel:remove")
    @Log(title = "车辆加油记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        mineCarRefuelService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
