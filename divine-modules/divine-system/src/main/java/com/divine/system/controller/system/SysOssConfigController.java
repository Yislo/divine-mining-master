package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.core.validate.QueryGroup;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.web.core.BaseController;
import com.divine.system.domain.dto.SysOssConfigDto;
import com.divine.system.domain.vo.SysOssConfigVo;
import com.divine.system.service.SysOssConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "对象存储配置")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss/config")
public class SysOssConfigController extends BaseController {

    private final SysOssConfigService sysOssConfigService;

    @Operation(summary = "查询对象存储配置列表")
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public PageInfoRes<SysOssConfigVo> list(@Validated(QueryGroup.class) SysOssConfigDto bo, BasePage basePage) {
        return sysOssConfigService.queryPageList(bo, basePage);
    }

    @Operation(summary = "获取对象存储配置详细信息")
    @SaCheckPermission("system:oss:query")
    @GetMapping("/{ossConfigId}")
    public Result<SysOssConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long ossConfigId) {
        return Result.success(sysOssConfigService.queryById(ossConfigId));
    }

    @Operation(summary = "新增对象存储配置")
    @SaCheckPermission("system:oss:add")
    @Log(title = "对象存储配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody SysOssConfigDto bo) {
        return toAjax(sysOssConfigService.insertByBo(bo));
    }

    @Operation(summary = "修改对象存储配置")
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody SysOssConfigDto bo) {
        return toAjax(sysOssConfigService.updateByBo(bo));
    }

    /**
     * 删除对象存储配置
     *
     * @param ossConfigIds OSS配置ID串
     */
    @Operation(summary = "删除对象存储配置")
    @SaCheckPermission("system:oss:remove")
    @Log(title = "对象存储配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossConfigIds}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ossConfigIds) {
        return toAjax(sysOssConfigService.deleteWithValidByIds(List.of(ossConfigIds), true));
    }

    @Operation(summary = "状态修改")
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Void> changeStatus(@RequestBody SysOssConfigDto bo) {
        return toAjax(sysOssConfigService.updateOssConfigStatus(bo));
    }
}
