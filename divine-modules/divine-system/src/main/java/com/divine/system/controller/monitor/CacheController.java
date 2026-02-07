package com.divine.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import com.divine.common.core.domain.Result;
import com.divine.system.domain.entity.SysCache;
import com.divine.common.core.constant.CacheConstants;
import com.divine.common.core.constant.CacheNames;
import com.divine.common.core.utils.StreamUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.json.utils.JsonUtils;
import com.divine.common.redis.utils.CacheUtils;
import com.divine.common.redis.utils.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Tag(name = "缓存监控")
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {

    private final RedissonConnectionFactory connectionFactory;

    private final static List<SysCache> CACHES = new ArrayList<>();

    static {
        CACHES.add(new SysCache(CacheConstants.ONLINE_TOKEN_KEY, "在线用户"));
        CACHES.add(new SysCache(CacheNames.SYS_CONFIG, "配置信息"));
        CACHES.add(new SysCache(CacheNames.SYS_DICT, "数据字典"));
        CACHES.add(new SysCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        CACHES.add(new SysCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        CACHES.add(new SysCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        CACHES.add(new SysCache(CacheNames.SYS_OSS_CONFIG, "OSS配置"));
        CACHES.add(new SysCache(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
    }

    @Operation(summary = "获取缓存监控列表")
    @SaCheckPermission("monitor:cache:list")
    @GetMapping()
    public Result<Map<String, Object>> getInfo() throws Exception {
        RedisConnection connection = connectionFactory.getConnection();
        Properties commandStats = connection.commands().info("commandstats");

        List<Map<String, String>> pieList = new ArrayList<>();
        if (commandStats != null) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }
        return Result.success(Map.of(
            "info", connection.commands().info(),
            "dbSize", connection.commands().dbSize(),
            "commandStats", pieList
        ));
    }

    @Operation(summary = "获取缓存监控缓存名列表")
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getNames")
    public Result<List<SysCache>> cache() {
        return Result.success(CACHES);
    }

    @Operation(summary = "获取缓存监控Key列表")
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getKeys/{cacheName}")
    public Result<Collection<String>> getCacheKeys(@PathVariable String cacheName) {
        Collection<String> cacheKeys = new HashSet<>(0);
        if (isCacheNames(cacheName)) {
            Set<Object> keys = CacheUtils.keys(cacheName);
            if (CollUtil.isNotEmpty(keys)) {
                cacheKeys = StreamUtils.toList(keys, Object::toString);
            }
        } else {
            cacheKeys = RedisUtils.keys(cacheName + "*");
        }
        return Result.success(cacheKeys);
    }

    @Operation(summary = "获取缓存监控缓存值详情")
    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public Result<SysCache> getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        Object cacheValue;
        if (isCacheNames(cacheName)) {
            cacheValue = CacheUtils.get(cacheName, cacheKey);
        } else {
            cacheValue = RedisUtils.getCacheObject(cacheKey);
        }
        SysCache sysCache = new SysCache(cacheName, cacheKey, JsonUtils.toJsonString(cacheValue));
        return Result.success(sysCache);
    }

    @Operation(summary = "清理缓存监控缓存名")
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheName/{cacheName}")
    public Result<Void> clearCacheName(@PathVariable String cacheName) {
        if (isCacheNames(cacheName)) {
            CacheUtils.clear(cacheName);
        } else {
            RedisUtils.deleteKeys(cacheName + "*");
        }
        return Result.success();
    }

    @Operation(summary = "清理缓存监控Key")
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheKey/{cacheName}/{cacheKey}")
    public Result<Void> clearCacheKey(@PathVariable String cacheName, @PathVariable String cacheKey) {
        if (isCacheNames(cacheName)) {
            CacheUtils.evict(cacheName, cacheKey);
        } else {
            RedisUtils.deleteObject(cacheKey);
        }
        return Result.success();
    }

    @Operation(summary = "清理全部缓存监控")
    @SaCheckPermission("monitor:cache:list")
    @DeleteMapping("/clearCacheAll")
    public Result<Void> clearCacheAll() {
        RedisUtils.deleteKeys("*");
        return Result.success();
    }

    private boolean isCacheNames(String cacheName) {
        return !StringUtils.contains(cacheName, ":");
    }
}
