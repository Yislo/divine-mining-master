package com.divine.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysLogininforDto;
import com.divine.common.log.annotation.Log;
import com.divine.common.core.constant.CacheConstants;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.entity.SysLogininfor;
import com.divine.system.domain.vo.SysLogininforVo;
import com.divine.system.service.SysLogininforService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "系统访问记录")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController {

    private final SysLogininforService sysLoginInforService;

    @Operation(summary = "获取系统访问记录列表")
    @SaCheckPermission("monitor:logininfor:list")
    @GetMapping("/list")
    public PageInfoRes<SysLogininforVo> list(SysLogininforDto logininfor, BasePage basePage) {
        return sysLoginInforService.selectPageLogininforList(logininfor, basePage);
    }

    @Operation(summary = "导出系统访问记录列表")
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:logininfor:export")
    @PostMapping("/export")
    public void export(SysLogininforDto logininfor, HttpServletResponse response) {
        List<SysLogininfor> list = sysLoginInforService.selectLogininforList(logininfor);
        ExcelUtil.exportExcel(list, "登录日志", SysLogininfor.class, response);
    }

    /**
     * 批量删除登录日志
     * @param infoIds 日志ids
     */
    @Operation(summary = "批量删除登录日志")
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public Result<Void> remove(@PathVariable Long[] infoIds) {
        return toAjax(sysLoginInforService.deleteLogininforByIds(infoIds));
    }

    @Operation(summary = "清理系统访问记录")
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public Result<Void> clean() {
        sysLoginInforService.cleanLogininfor();
        return Result.success();
    }

    @Operation(summary = "账户解锁")
    @SaCheckPermission("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public Result<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return Result.success();
    }

}
