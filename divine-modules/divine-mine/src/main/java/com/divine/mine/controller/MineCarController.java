package com.divine.mine.controller;

import java.util.List;

import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineCarDto;
import com.divine.mine.domain.vo.MineCarVo;
import com.divine.mine.service.MineCarService;
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
 * 车辆信息
 *
 * @author yisl
 * @date 2026-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/car")
public class MineCarController extends BaseController {

    private final MineCarService mineCarService;

    /**
     * 查询车辆信息列表
     */
//    @SaCheckPermission("car:list")
    @GetMapping("/list")
    public PageInfoRes<MineCarVo> list(MineCarDto dto, BasePage basePage) {
        return mineCarService.queryPageList(dto, basePage);
    }

    /**
     * 导出车辆信息列表
     */
//    @SaCheckPermission("car:export")
    @Log(title = "车辆信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MineCarDto dto, HttpServletResponse response) {
        List<MineCarVo> list = mineCarService.queryList(dto);
        ExcelUtil.exportExcel(list, "车辆信息", MineCarVo.class, response);
    }

    /**
     * 获取车辆信息详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("car:query")
    @GetMapping("/{id}")
    public Result<MineCarVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return Result.success(mineCarService.queryById(id));
    }

    /**
     * 新增车辆信息
     */
//    @SaCheckPermission("car:add")
    @Log(title = "车辆信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody MineCarDto dto) {
        mineCarService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改车辆信息
     */
//    @SaCheckPermission("car:edit")
    @Log(title = "车辆信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody MineCarDto dto) {
        mineCarService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除车辆信息
     *
     * @param ids 主键串
     */
//    @SaCheckPermission("car:remove")
    @Log(title = "车辆信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                               @PathVariable Long[] ids) {
        mineCarService.deleteByIds(List.of(ids));
        return Result.success();
    }

    /**
     * 获取所有车辆列表
     *
     */
//    @SaCheckPermission("car:remove")
    @GetMapping("/listNoPage")
    public Result<List<MineCarVo>> listNoPage() {
        return Result.success(mineCarService.queryList(new MineCarDto()));
    }
}
