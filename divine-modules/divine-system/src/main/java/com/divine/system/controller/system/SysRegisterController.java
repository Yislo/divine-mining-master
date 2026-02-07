package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.divine.common.core.domain.Result;
import com.divine.common.web.core.BaseController;
import com.divine.common.core.domain.dto.RegisterBody;
import com.divine.system.service.SysConfigService;
import com.divine.system.service.SysRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "注册验证")
@Validated
@RequiredArgsConstructor
@RestController
public class SysRegisterController extends BaseController {

    private final SysRegisterService registerService;
    private final SysConfigService configService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @SaIgnore
    @PostMapping("/register")
    public Result<Void> register(@Validated @RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return Result.fail("当前系统没有开启注册功能！");
        }
        registerService.register(user);
        return Result.success();
    }
}
