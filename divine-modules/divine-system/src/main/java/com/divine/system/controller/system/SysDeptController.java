package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.convert.Convert;
import com.divine.common.core.domain.Result;
import com.divine.common.log.annotation.Log;
import com.divine.common.core.constant.UserConstants;
import com.divine.common.web.core.BaseController;
import com.divine.system.domain.dto.SysDeptDto;
import com.divine.common.log.enums.BusinessType;
import com.divine.common.core.utils.StringUtils;
import com.divine.system.domain.vo.SysDeptVo;
import com.divine.system.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "部门信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    private final SysDeptService deptService;

    @Operation(summary = "获取部门列表")
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list")
    public Result<List<SysDeptVo>> list(SysDeptDto dept) {
        List<SysDeptVo> depts = deptService.selectDeptList(dept);
        return Result.success(depts);
    }

    @Operation(summary = "查询部门列表（排除节点）")
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    public Result<List<SysDeptVo>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDeptVo> depts = deptService.selectDeptList(new SysDeptDto());
        depts.removeIf(d -> d.getDeptId().equals(deptId)
            || StringUtils.splitList(d.getAncestors()).contains(Convert.toStr(deptId)));
        return Result.success(depts);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @SaCheckPermission("system:dept:query")
    @GetMapping(value = "/{deptId}")
    public Result<SysDeptVo> getInfo(@PathVariable Long deptId) {
        deptService.checkDeptDataScope(deptId);
        return Result.success(deptService.selectDeptById(deptId));
    }

    @Operation(summary = "新增部门")
    @SaCheckPermission("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysDeptDto dept) {
        if (!deptService.checkDeptNameUnique(dept)) {
            return Result.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return toAjax(deptService.insertDept(dept));
    }

    @Operation(summary = "修改部门")
    @SaCheckPermission("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysDeptDto dept) {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (!deptService.checkDeptNameUnique(dept)) {
            return Result.fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return Result.fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())) {
            if (deptService.selectNormalChildrenDeptById(deptId) > 0) {
                return Result.fail("该部门包含未停用的子部门!");
            } else if (deptService.checkDeptExistUser(deptId)) {
                return Result.fail("该部门下存在已分配用户，不能禁用!");
            }
        }
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     */
    @Operation(summary = "删除部门")
    @SaCheckPermission("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public Result<Void> remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Result.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.warn("部门存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
