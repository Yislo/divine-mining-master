package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.common.core.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Tag(name = "测试国际化")
@Validated
@RestController
@RequestMapping("/demo/i18n")
public class TestI18nController {

    /**
     * 通过code获取国际化内容
     * code为 messages.properties 中的 key
     * <p>
     * 测试使用 user.register.success
     *
     * @param code 国际化code
     */
    @Operation(summary = "通过code获取国际化内容")
    @GetMapping()
    public Result<Void> get(String code) {
        return Result.success(MessageUtils.message(code));
    }

    /**
     * Validator 校验国际化
     * 不传值 分别查看异常返回
     * <p>
     * 测试使用 not.null
     */
    @Operation(summary = "Validator 校验国际化")
    @GetMapping("/test1")
    public Result<Void> test1(@NotBlank(message = "{not.null}") String str) {
        return Result.success(str);
    }

    /**
     * Bean 校验国际化
     * 不传值 分别查看异常返回
     * <p>
     * 测试使用 not.null
     */
    @Operation(summary = "Bean 校验国际化")
    @GetMapping("/test2")
    public Result<TestI18nBo> test2(@Validated TestI18nBo bo) {
        return Result.success(bo);
    }

    @Data
    public static class TestI18nBo {

        @NotBlank(message = "{not.null}")
        private String name;

        @NotNull(message = "{not.null}")
        @Range(min = 0, max = 100, message = "{length.not.valid}")
        private Integer age;
    }
}
