package com.divine.mine.controller;

import java.util.List;

import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.mine.domain.dto.MineAccessRecordDto;
import com.divine.mine.domain.vo.MineAccessRecordVo;
import com.divine.mine.service.impl.MineAccessRecordServiceImpl;
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
import com.divine.common.mybatis.core.page.PageInfoRes;

/**
 * 车辆出入厂记录
 *
 * @author yisl
 * @date 2026-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/accessRecord")
public class MineAccessRecordController extends BaseController {

    private final MineAccessRecordServiceImpl mineAccessRecordServiceImpl;

    /**
     * 查询车辆出入厂记录列表
     */
//    @SaCheckPermission("access:accessRecord:list")
    @GetMapping("/list")
    public PageInfoRes<MineAccessRecordVo> list(MineAccessRecordDto dto, BasePage basePage) {
        return mineAccessRecordServiceImpl.queryPageList(dto, basePage);
    }

    /**
     * 导出车辆出入厂记录列表
     */
//    @SaCheckPermission("access:accessRecord:export")
    @Log(title = "车辆出入厂记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MineAccessRecordDto dto, HttpServletResponse response) {
        List<MineAccessRecordVo> list = mineAccessRecordServiceImpl.queryList(dto);
        ExcelUtil.exportExcel(list, "车辆出入厂记录", MineAccessRecordVo.class, response);
    }

    /**
     * 获取车辆出入厂记录详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("access:accessRecord:query")
    @GetMapping("/{id}")
    public Result<MineAccessRecordVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return Result.success(mineAccessRecordServiceImpl.queryById(id));
    }

    /**
     * 新增车辆出入厂记录
     */
//    @SaCheckPermission("access:accessRecord:add")
    @Log(title = "车辆出入厂记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MineAccessRecordDto dto) {
        mineAccessRecordServiceImpl.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改车辆出入厂记录
     */
//    @SaCheckPermission("access:accessRecord:edit")
    @Log(title = "车辆出入厂记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MineAccessRecordDto dto) {
        mineAccessRecordServiceImpl.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除车辆出入厂记录
     *
     * @param ids 主键串
     */
//    @SaCheckPermission("access:accessRecord:remove")
    @Log(title = "车辆出入厂记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        mineAccessRecordServiceImpl.deleteByIds(List.of(ids));
        return Result.success();
    }
}
