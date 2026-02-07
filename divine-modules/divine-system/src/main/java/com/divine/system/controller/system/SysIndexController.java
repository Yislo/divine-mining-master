package com.divine.system.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.divine.common.core.config.DivineConfig;
import com.divine.common.core.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "首页")
@RequiredArgsConstructor
@RestController
public class SysIndexController {

    /**
     * 系统基础配置
     */
    private final DivineConfig divineConfig;


    @Operation(summary = "访问首页，提示语")
    @SaIgnore
    @GetMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", divineConfig.getName(), divineConfig.getVersion());
    }
}
