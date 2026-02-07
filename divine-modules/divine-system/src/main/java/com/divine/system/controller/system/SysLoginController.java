package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.divine.common.core.domain.Result;
import com.divine.system.domain.entity.SysMenu;
import com.divine.system.domain.vo.RouterVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.service.SysLoginService;
import com.divine.system.service.SysMenuService;
import com.divine.system.service.SysUserService;
import com.divine.common.core.constant.Constants;
import com.divine.common.core.domain.dto.EmailLoginBody;
import com.divine.common.core.domain.dto.LoginBody;
import com.divine.common.core.domain.dto.LoginUser;
import com.divine.common.core.domain.dto.SmsLoginBody;
import com.divine.common.satoken.utils.LoginHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "登录验证")
@Validated
@RequiredArgsConstructor
@RestController
public class SysLoginController {

    private final SysLoginService loginService;
    private final SysMenuService menuService;
    private final SysUserService userService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "登录")
    @SaIgnore
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
            loginBody.getUuid());
        return Result.success(Map.of(Constants.TOKEN, token));
    }

    /**
     * 短信登录
     *
     * @param smsLoginBody 登录信息
     * @return 结果
     */
    @Operation(summary = "短信登录")
    @SaIgnore
    @PostMapping("/smsLogin")
    public Result<Map<String, Object>> smsLogin(@Validated @RequestBody SmsLoginBody smsLoginBody) {
        // 生成令牌
        String token = loginService.smsLogin(smsLoginBody.getPhonenumber(), smsLoginBody.getSmsCode());
        return Result.success(Map.of(Constants.TOKEN, token));
    }

    /**
     * 邮件登录
     *
     * @param body 登录信息
     * @return 结果
     */
    @Operation(summary = "邮件登录")
    @PostMapping("/emailLogin")
    public Result<Map<String, Object>> emailLogin(@Validated @RequestBody EmailLoginBody body) {
        // 生成令牌
        String token = loginService.emailLogin(body.getEmail(), body.getEmailCode());
        return Result.success(Map.of(Constants.TOKEN, token));
    }

    /**
     * 小程序登录(示例)
     *
     * @param xcxCode 小程序code
     * @return 结果
     */
    @Operation(summary = "小程序登录(示例)")
    @SaIgnore
    @PostMapping("/xcxLogin")
    public Result<Map<String, Object>> xcxLogin(@NotBlank(message = "{xcx.code.not.blank}") String xcxCode) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.xcxLogin(xcxCode);
        return Result.success(Map.of(Constants.TOKEN, token));
    }

    @Operation(summary = "退出登录")
    @SaIgnore
    @PostMapping("/logout")
    public Result<Void> logout() {
        loginService.logout();
        return Result.success("退出成功");
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("getInfo")
    public Result<Map<String, Object>> getInfo() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        SysUserVo user = userService.selectUserById(loginUser.getUserId());
        return Result.success(Map.of(
                "user", user,
                "roles", loginUser.getRolePermission(),
                "permissions", loginUser.getMenuPermission()
            )
        );
    }

    @Operation(summary = "获取路由信息")
    @GetMapping("getRouters")
    public Result<List<RouterVo>> getRouters() {
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(LoginHelper.getUserId());
        return Result.success(menuService.buildMenus(menus));
    }
}
