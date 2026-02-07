package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.log.annotation.Log;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.system.domain.dto.SysNoticeDto;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.system.service.SysNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "公告 信息操作处理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    private final SysNoticeService noticeService;

    @Operation(summary = "获取通知公告列表")
    @SaCheckPermission("system:notice:list")
    @GetMapping("/list")
    public PageInfoRes<SysNoticeVo> list(SysNoticeDto notice, BasePage basePage) {
        return noticeService.selectPageNoticeList(notice, basePage);
    }

    /**
     * 根据通知公告编号获取详细信息
     *
     * @param noticeId 公告ID
     */
    @Operation(summary = "根据通知公告编号获取详细信息")
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public Result<SysNoticeVo> getInfo(@PathVariable Long noticeId) {
        return Result.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @Operation(summary = "新增通知公告")
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysNoticeDto notice) {
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @Operation(summary = "修改通知公告")
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysNoticeDto notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     *
     * @param noticeIds 公告ID串
     */
    @Operation(summary = "删除通知公告")
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public Result<Void> remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
