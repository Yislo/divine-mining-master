package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.common.ratelimiter.annotation.RateLimiter;
import com.divine.common.ratelimiter.enums.LimitType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试分布式限流样例")
@Slf4j
@RestController
@RequestMapping("/demo/rateLimiter")
public class RedisRateLimiterController {

    @Operation(summary = "测试全局限流(全局影响)")
    @RateLimiter(count = 2, time = 10)
    @GetMapping("/test")
    public Result<String> test(String value) {
        return Result.success("操作成功", value);
    }

    @Operation(summary = "测试请求IP限流(同一IP请求受影响)")
    @RateLimiter(count = 2, time = 10, limitType = LimitType.IP)
    @GetMapping("/testip")
    public Result<String> testip(String value) {
        return Result.success("操作成功", value);
    }

    @Operation(summary = "测试集群实例限流(启动两个后端服务互不影响)")
    @RateLimiter(count = 2, time = 10, limitType = LimitType.CLUSTER)
    @GetMapping("/testcluster")
    public Result<String> testcluster(String value) {
        return Result.success("操作成功", value);
    }


    @Operation(summary = "测试请求IP限流(key基于参数获取)，同一IP请求受影响，简单变量获取 #变量 复杂表达式 #{#变量 != 1 ? 1 : 0}")
    @RateLimiter(count = 2, time = 10, limitType = LimitType.IP, key = "#value")
    @GetMapping("/testObj")
    public Result<String> testObj(String value) {
        return Result.success("操作成功", value);
    }

}
