package com.divine.system.controller.system;

import java.util.List;

import com.divine.common.core.domain.Result;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.idempotent.annotation.RepeatSubmit;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.system.domain.dto.SysNoticeRuleDto;
import com.divine.system.domain.vo.SysNoticeRuleVo;
import com.divine.system.service.SysNoticeRuleService;
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
 * 消息推送规则
 *
 * @author yisl
 * @date 2026-03-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/noticeRule")
public class SysNoticeRuleController extends BaseController {

    private final SysNoticeRuleService sysNoticeRuleService;

    /**
     * 查询消息推送规则列表
     */
    @SaCheckPermission("wms:noticeRule:list")
    @GetMapping("/list")
    public PageInfoRes<SysNoticeRuleVo> list(SysNoticeRuleDto dto, BasePage basePage) {
        return sysNoticeRuleService.queryPageList(dto, basePage);
    }


    /**
     * 新增消息推送规则
     */
    @SaCheckPermission("wms:noticeRule:add")
    @Log(title = "消息推送规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Result<Void> add(@Validated(AddGroup.class) @RequestBody SysNoticeRuleDto dto) {
        sysNoticeRuleService.insertByBo(dto);
        return Result.success();
    }

    /**
     * 修改消息推送规则
     */
    @SaCheckPermission("wms:noticeRule:edit")
    @Log(title = "消息推送规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public Result<Void> edit(@Validated(EditGroup.class) @RequestBody SysNoticeRuleDto dto) {
        sysNoticeRuleService.updateByBo(dto);
        return Result.success();
    }

    /**
     * 删除消息推送规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:noticeRule:remove")
    @Log(title = "消息推送规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        sysNoticeRuleService.deleteByIds(List.of(ids));
        return Result.success();
    }
}
