package com.divine.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.log.annotation.Log;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.dto.SysOperLogDto;
import com.divine.system.domain.vo.SysOperLogVo;
import com.divine.system.service.SysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "操作日志记录")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

    private final SysOperLogService operLogService;

    @Operation(summary = "获取操作日志记录列表")
    @SaCheckPermission("monitor:operlog:list")
    @GetMapping("/list")
    public PageInfoRes<SysOperLogVo> list(SysOperLogDto operLog, BasePage basePage) {
        return operLogService.selectPageOperLogList(operLog, basePage);
    }

    @Operation(summary = "导出操作日志记录列表")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:operlog:export")
    @PostMapping("/export")
    public void export(SysOperLogDto operLog, HttpServletResponse response) {
        List<SysOperLogVo> list = operLogService.selectOperLogList(operLog);
        ExcelUtil.exportExcel(list, "操作日志", SysOperLogVo.class, response);
    }

    /**
     * 批量删除操作日志记录
     * @param operIds 日志ids
     */
    @Operation(summary = "批量删除操作日志记录")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    public Result<Void> remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Operation(summary = "清理操作日志记录")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/clean")
    public Result<Void> clean() {
        operLogService.cleanOperLog();
        return Result.success();
    }
}
