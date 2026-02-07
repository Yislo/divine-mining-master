package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysConfigDto;
import com.divine.common.log.annotation.Log;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.system.service.SysConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "参数配置 信息操作处理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    private final SysConfigService configService;

    @Operation(summary = "获取参数配置列表")
    @SaCheckPermission("system:config:list")
    @GetMapping("/list")
    public PageInfoRes<SysConfigVo> list(SysConfigDto config, BasePage basePage) {
        return configService.selectPageConfigList(config, basePage);
    }

    @Operation(summary = "导出参数配置列表")
    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:config:export")
    @PostMapping("/export")
    public void export(SysConfigDto config, HttpServletResponse response) {
        List<SysConfigVo> list = configService.selectConfigList(config);
        ExcelUtil.exportExcel(list, "参数数据", SysConfigVo.class, response);
    }

    @Operation(summary = "根据参数编号获取详细信息")
    @SaCheckPermission("system:config:query")
    @GetMapping(value = "/{configId}")
    public Result<SysConfigVo> getInfo(@PathVariable Long configId) {
        return Result.success(configService.selectConfigById(configId));
    }

    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    public Result<Void> getConfigKey(@PathVariable String configKey) {
        return Result.success(configService.selectConfigByKey(configKey));
    }

    @Operation(summary = "新增参数配置")
    @SaCheckPermission("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysConfigDto config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return Result.fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.insertConfig(config);
        return Result.success();
    }

    @Operation(summary = "修改参数配置")
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysConfigDto config) {
        if (!configService.checkConfigKeyUnique(config)) {
            return Result.fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        configService.updateConfig(config);
        return Result.success();
    }

    @Operation(summary = "根据参数键名修改参数配置")
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public Result<Void> updateByKey(@RequestBody SysConfigDto config) {
        configService.updateConfig(config);
        return Result.success();
    }

    @Operation(summary = "删除参数配置")
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public Result<Void> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return Result.success();
    }

    @Operation(summary = "刷新参数缓存")
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public Result<Void> refreshCache() {
        configService.resetConfigCache();
        return Result.success();
    }
}
