package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysRoleDto;
import com.divine.system.domain.dto.SysUserDto;
import com.divine.system.domain.entity.SysUserRole;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.common.log.annotation.Log;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.web.core.BaseController;
import com.divine.system.domain.dto.SysDeptDto;
import com.divine.system.domain.vo.SysRoleVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.service.SysDeptService;
import com.divine.system.service.SysRoleService;
import com.divine.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "角色信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    private final SysRoleService roleService;
    private final SysUserService userService;
    private final SysDeptService deptService;

    /**
     * 获取角色信息列表
     */
    @Operation(summary = "获取角色信息列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    public PageInfoRes<SysRoleVo> list(SysRoleDto role, BasePage basePage) {
        return roleService.selectPageRoleList(role, basePage);
    }

    /**
     * 导出角色信息列表
     */
    @Operation(summary = "导出角色信息列表")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:role:export")
    @PostMapping("/export")
    public void export(SysRoleDto role, HttpServletResponse response) {
        List<SysRoleVo> list = roleService.selectRoleList(role);
        ExcelUtil.exportExcel(list, "角色数据", SysRoleVo.class, response);
    }

    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色ID
     */
    @Operation(summary = "根据角色编号获取详细信息")
    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/{roleId}")
    public Result<SysRoleVo> getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return Result.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色")
    @SaCheckPermission("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysRoleDto role) {
        roleService.checkRoleAllowed(role);
        if (!roleService.checkRoleNameUnique(role)) {
            return Result.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return Result.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.insertRole(role));

    }

    /**
     * 修改保存角色
     */
    @Operation(summary = "修改保存角色")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysRoleDto role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            return Result.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return Result.fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }

        if (roleService.updateRole(role) > 0) {
            roleService.cleanOnlineUserByRole(role.getRoleId());
            return Result.success();
        }
        return Result.fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    /**
     * 修改保存数据权限
     */
    @Operation(summary = "修改保存数据权限")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public Result<Void> dataScope(@RequestBody SysRoleDto role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * 状态修改
     */
    @Operation(summary = "状态修改")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Result<Void> changeStatus(@RequestBody SysRoleDto role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.updateRoleStatus(role.getRoleId(), role.getStatus()));
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID串
     */
    @Operation(summary = "删除角色")
    @SaCheckPermission("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public Result<Void> remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 获取角色选择框列表
     */
    @Operation(summary = "获取角色选择框列表")
    @SaCheckPermission("system:role:query")
    @GetMapping("/optionselect")
    public Result<List<SysRoleVo>> optionselect() {
        return Result.success(roleService.selectRoleAll());
    }

    /**
     * 查询已分配用户角色列表
     */
    @Operation(summary = "查询已分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/allocatedList")
    public PageInfoRes<SysUserVo> allocatedList(SysUserDto user, BasePage basePage) {
        return userService.selectAllocatedList(user, basePage);
    }

    /**
     * 查询未分配用户角色列表
     */
    @Operation(summary = "查询未分配用户角色列表")
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    public PageInfoRes<SysUserVo> unallocatedList(SysUserDto user, BasePage basePage) {
        return userService.selectUnallocatedList(user, basePage);
    }

    /**
     * 取消授权用户
     */
    @Operation(summary = "取消授权用户")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public Result<Void> cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * 批量取消授权用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @Operation(summary = "批量取消授权用户")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public Result<Void> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @Operation(summary = "批量选择用户授权")
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public Result<Void> selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 获取对应角色部门树列表
     *
     * @param roleId 角色ID
     */
    @Operation(summary = "获取对应角色部门树列表")
    @SaCheckPermission("system:role:list")
    @GetMapping(value = "/deptTree/{roleId}")
    public Result<Map<String, Object>> roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        return Result.success(Map.of(
            "checkedKeys", deptService.selectDeptListByRoleId(roleId),
            "depts", deptService.selectDeptTreeList(new SysDeptDto())
        ));
    }
}
