package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysRoleDto;
import com.divine.system.domain.dto.SysUserDto;
import com.divine.system.service.SysDeptService;
import com.divine.system.service.SysPostService;
import com.divine.system.service.SysRoleService;
import com.divine.system.service.SysUserService;
import com.divine.common.core.constant.UserConstants;
import com.divine.common.core.utils.StreamUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.excel.core.ExcelResult;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.satoken.utils.LoginHelper;
import com.divine.common.web.core.BaseController;
import com.divine.system.domain.dto.SysDeptDto;
import com.divine.system.domain.dto.SysPostDto;
import com.divine.system.domain.vo.SysRoleVo;
import com.divine.system.domain.vo.SysUserExportVo;
import com.divine.system.domain.vo.SysUserImportVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.listener.SysUserImportListener;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "用户信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
@Slf4j
public class SysUserController extends BaseController {

    private final SysUserService userService;
    private final SysRoleService roleService;
    private final SysPostService postService;
    private final SysDeptService deptService;


    @Operation(summary = "获取用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    public PageInfoRes<SysUserVo> list(SysUserDto user, BasePage basePage) {
        return userService.selectPageUserList(user, basePage);
    }

    @Operation(summary = "导出用户列表")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:user:export")
    @PostMapping("/export")
    public void export(SysUserDto user, HttpServletResponse response) {
        List<SysUserExportVo> list = userService.selectUserExportList(user);
        ExcelUtil.exportExcel(list, "用户数据", SysUserExportVo.class, response);
    }

    /**
     * 导入数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Operation(summary = "导入数据")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @SaCheckPermission("system:user:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        ExcelResult<SysUserImportVo> result = ExcelUtil.importExcel(file.getInputStream(), SysUserImportVo.class, new SysUserImportListener(updateSupport));
        return Result.success(result.getAnalysis());
    }

    @Operation(summary = "获取导入模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "用户数据", SysUserImportVo.class, response);
    }

    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户ID
     */
    @Operation(summary = "根据用户编号获取详细信息")
    @SaCheckPermission("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public Result<Map<String, Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        Map<String, Object> ajax = new HashMap<>();
        SysRoleDto role = new SysRoleDto();
        role.setStatus(UserConstants.ROLE_NORMAL);
        SysPostDto post = new SysPostDto();
        post.setStatus(UserConstants.POST_NORMAL);
        List<SysRoleVo> roles = roleService.selectRoleList(role);
        ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
        ajax.put("posts", postService.selectPostList(post));
        if (ObjectUtil.isNotNull(userId)) {
            SysUserVo sysUser = userService.selectUserById(userId);
            ajax.put("user", sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", StreamUtils.toList(sysUser.getRoles(), SysRoleVo::getRoleId));
        }
        return Result.success(ajax);
    }

    @Operation(summary = "新增用户")
    @SaCheckPermission("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysUserDto user) {
        deptService.checkDeptDataScope(user.getDeptId());
        if (!userService.checkUserNameUnique(user)) {
            return Result.fail("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return Result.fail("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return Result.fail("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("123456"));
    }


    @Operation(summary = "修改用户")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysUserDto user) {
        userService.checkUserAllowed(user.getUserId());
        userService.checkUserDataScope(user.getUserId());
        deptService.checkDeptDataScope(user.getDeptId());
        if (!userService.checkUserNameUnique(user)) {
            return Result.fail("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return Result.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return Result.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     *
     * @param userIds 角色ID串
     */
    @Operation(summary = "删除用户")
    @SaCheckPermission("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public Result<Void> remove(@PathVariable Long[] userIds) {
        if (ArrayUtil.contains(userIds, LoginHelper.getUserId())) {
            return Result.fail("当前用户不能删除");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    @Operation(summary = "重置密码")
    @SaCheckPermission("system:user:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public Result<Void> resetPwd(@RequestBody SysUserDto user) {
        userService.checkUserAllowed(user.getUserId());
        userService.checkUserDataScope(user.getUserId());
        String hashpw = BCrypt.hashpw(user.getPassword());
        log.info("user.getPassword：{}",user.getPassword());
        log.info("hashpw:{}", hashpw);
        user.setPassword(hashpw);
        return toAjax(userService.resetUserPwd(user.getUserId(), user.getPassword()));
    }

    @Operation(summary = "状态修改")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Void> changeStatus(@RequestBody SysUserDto user) {
        userService.checkUserAllowed(user.getUserId());
        userService.checkUserDataScope(user.getUserId());
        return toAjax(userService.updateUserStatus(user.getUserId(), user.getStatus()));
    }

    /**
     * 根据用户编号获取授权角色
     *
     * @param userId 用户ID
     */
    @Operation(summary = "根据用户编号获取授权角色")
    @SaCheckPermission("system:user:query")
    @GetMapping("/authRole/{userId}")
    public Result<Map<String, Object>> authRole(@PathVariable Long userId) {
        SysUserVo user = userService.selectUserById(userId);
        List<SysRoleVo> roles = roleService.selectRolesAuthByUserId(userId);
        return Result.success(Map.of(
            "user", user,
            "roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin())
        ));
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户Id
     * @param roleIds 角色ID串
     */
    @Operation(summary = "用户授权角色")
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public Result<Void> insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        userService.insertUserAuth(userId, roleIds);
        return Result.success();
    }

    @Operation(summary = "获取部门树列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    public Result<List<Tree<Long>>> deptTree(SysDeptDto dept) {
        return Result.success(deptService.selectDeptTreeList(dept));
    }

}
