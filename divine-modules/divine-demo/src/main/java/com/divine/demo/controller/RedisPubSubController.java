package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.common.redis.utils.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Redis发布订阅 演示案例")
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/redis/pubsub")
public class RedisPubSubController {

    @Operation(summary = "发布消息")
    @GetMapping("/pub")
    public Result<Void> pub(@Schema(description = "通道Key") String key,
                            @Schema(description = "发送内容") String value) {
        RedisUtils.publish(key, value, consumer -> {
            System.out.println("发布通道 => " + key + ", 发送值 => " + value);
        });
        return Result.success("操作成功");
    }

    @Operation(summary = "发布消息")
    @GetMapping("/sub")
    public Result<Void> sub(@Schema(description = "通道Key") String key) {
        RedisUtils.subscribe(key, String.class, msg -> {
            System.out.println("订阅通道 => " + key + ", 接收值 => " + msg);
        });
        return Result.success("操作成功");
    }

}
