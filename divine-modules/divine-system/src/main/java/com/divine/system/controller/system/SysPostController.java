package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.log.annotation.Log;
import com.divine.common.core.constant.UserConstants;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.web.core.BaseController;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.domain.dto.SysPostDto;
import com.divine.system.domain.vo.SysPostVo;
import com.divine.system.service.SysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author Lion Li
 */
@Tag(name = "岗位信息操作处理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {

    private final SysPostService postService;

    /**
     * 获取岗位列表
     */
    @Operation(summary = "获取岗位列表")
    @SaCheckPermission("system:post:list")
    @GetMapping("/list")
    public PageInfoRes<SysPostVo> list(SysPostDto post, BasePage basePage) {
        return postService.selectPagePostList(post, basePage);
    }

    /**
     * 导出岗位列表
     */
    @Operation(summary = "导出岗位列表")
    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:post:export")
    @PostMapping("/export")
    public void export(SysPostDto post, HttpServletResponse response) {
        List<SysPostVo> list = postService.selectPostList(post);
        ExcelUtil.exportExcel(list, "岗位数据", SysPostVo.class, response);
    }

    /**
     * 根据岗位编号获取详细信息
     *
     * @param postId 岗位ID
     */
    @Operation(summary = "根据岗位编号获取详细信息")
    @SaCheckPermission("system:post:query")
    @GetMapping(value = "/{postId}")
    public Result<SysPostVo> getInfo(@PathVariable Long postId) {
        return Result.success(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @Operation(summary = "新增岗位")
    @SaCheckPermission("system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysPostDto post) {
        if (!postService.checkPostNameUnique(post)) {
            return Result.fail("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return Result.fail("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        return toAjax(postService.insertPost(post));
    }

    /**
     * 修改岗位
     */
    @Operation(summary = "修改岗位")
    @SaCheckPermission("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysPostDto post) {
        if (!postService.checkPostNameUnique(post)) {
            return Result.fail("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (!postService.checkPostCodeUnique(post)) {
            return Result.fail("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        } else if (UserConstants.POST_DISABLE.equals(post.getStatus())
            && postService.countUserPostById(post.getPostId()) > 0) {
            return Result.fail("该岗位下存在已分配用户，不能禁用!");
        }
        return toAjax(postService.updatePost(post));
    }

    /**
     * 删除岗位
     *
     * @param postIds 岗位ID串
     */
    @Operation(summary = "删除岗位")
    @SaCheckPermission("system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public Result<Void> remove(@PathVariable Long[] postIds) {
        return toAjax(postService.deletePostByIds(postIds));
    }

    /**
     * 获取岗位选择框列表
     */
    @Operation(summary = "获取岗位选择框列表")
    @GetMapping("/optionselect")
    public Result<List<SysPostVo>> optionselect() {
        SysPostDto post = new SysPostDto();
        post.setStatus(UserConstants.POST_NORMAL);
        List<SysPostVo> posts = postService.selectPostList(post);
        return Result.success(posts);
    }
}
