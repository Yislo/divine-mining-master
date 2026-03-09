package com.divine.common.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import java.util.HashMap;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Component
public class RedisDistributedLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX = "lock:";
    private static final long DEFAULT_EXPIRE = 30;  // 锁默认过期时间：30秒
    private static final long DEFAULT_WAIT = 3000;  // 获取锁最大等待时间：3000毫秒

    /**
     * 线程锁重入记录
     * key: lockKey
     * value: 当前线程获取锁的次数（用于可重入锁）
     */
    private static final ThreadLocal<Map<String, Integer>> LOCK_COUNT =
        ThreadLocal.withInitial(HashMap::new);

    /**
     * 看门狗线程池，用于自动续期锁
     */
    private static final ScheduledExecutorService WATCH_DOG =
        Executors.newScheduledThreadPool(2);

    /**
     * 续期任务映射表
     * key: lockKey:requestId
     * value: 定时任务Future
     */
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> RENEW_MAP =
        new ConcurrentHashMap<>();

    /**
     * Redis解锁脚本（Lua脚本，确保原子操作）
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;

    /**
     * Redis续期脚本（Lua脚本，确保原子操作）
     */
    private static final DefaultRedisScript<Long> RENEW_SCRIPT;

    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText(
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else return 0 end"
        );
        UNLOCK_SCRIPT.setResultType(Long.class);

        RENEW_SCRIPT = new DefaultRedisScript<>();
        RENEW_SCRIPT.setScriptText(
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('expire', KEYS[1], ARGV[2]) " +
                "else return 0 end"
        );
        RENEW_SCRIPT.setResultType(Long.class);
    }

    /**
     * 构建锁在Redis中的key
     * @param key 业务锁名
     * @return Redis中实际使用的key
     */
    private String buildKey(String key) {
        return LOCK_PREFIX + key;
    }

    /**
     * 获取锁（使用默认过期时间和默认等待时间）
     * @param lockKey 业务锁名
     * @return 如果获取成功返回requestId，用于解锁；否则返回null
     */
    public String lock(String lockKey) {
        String requestId = UUID.randomUUID().toString();
        boolean success = lock(lockKey, requestId, DEFAULT_EXPIRE, DEFAULT_WAIT);
        return success ? requestId : null;
    }

    /**
     * 获取锁（可重入锁）
     * @param lockKey 业务锁名
     * @param requestId 请求唯一标识
     * @param expireSeconds 锁过期时间（秒）
     * @param waitMillis 最大等待时间（毫秒）
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String requestId,
                        long expireSeconds, long waitMillis) {

        Map<String, Integer> map = LOCK_COUNT.get();

        // 可重入锁：如果当前线程已经持有锁，直接增加计数
        if (map.containsKey(lockKey)) {
            map.put(lockKey, map.get(lockKey) + 1);
            return true;
        }

        long end = System.currentTimeMillis() + waitMillis;

        // 尝试获取锁直到超时
        while (System.currentTimeMillis() < end) {
            Boolean success = redisTemplate.opsForValue().setIfAbsent(
                buildKey(lockKey),
                requestId,
                expireSeconds,
                TimeUnit.SECONDS
            );

            if (Boolean.TRUE.equals(success)) {
                map.put(lockKey, 1);  // 第一次获取锁，计数为1
                startWatchDog(lockKey, requestId, expireSeconds); // 启动看门狗续期
                return true;
            }

            // 等待一小段随机时间后重试，防止活锁
            try {
                Thread.sleep(30 + ThreadLocalRandom.current().nextInt(40));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }

    /**
     * 解锁
     * @param lockKey 业务锁名
     * @param requestId 请求唯一标识
     * @return 是否成功解锁
     */
    public boolean unlock(String lockKey, String requestId) {

        Map<String, Integer> map = LOCK_COUNT.get();
        Integer count = map.get(lockKey);

        if (count == null) {
            return false; // 当前线程没有持有锁
        }

        // 重入锁释放：如果计数大于1，减少计数，不真正释放锁
        if (count > 1) {
            map.put(lockKey, count - 1);
            return true;
        }

        // 计数为1，真正释放锁
        map.remove(lockKey);
        stopWatchDog(lockKey, requestId);

        Long result = redisTemplate.execute(
            UNLOCK_SCRIPT,
            Collections.singletonList(buildKey(lockKey)),
            requestId
        );

        return result != null && result > 0;
    }

    /**
     * 启动看门狗线程，定时续期锁
     * @param lockKey 业务锁名
     * @param requestId 请求唯一标识
     * @param expireSeconds 锁过期时间
     */
    private void startWatchDog(String lockKey, String requestId, long expireSeconds) {
        String taskKey = lockKey + ":" + requestId;
        long interval = expireSeconds * 1000 / 3; // 每1/3过期时间续期一次

        ScheduledFuture<?> future = WATCH_DOG.scheduleAtFixedRate(() -> {
            Long result = redisTemplate.execute(
                RENEW_SCRIPT,
                Collections.singletonList(buildKey(lockKey)),
                requestId,
                String.valueOf(expireSeconds)
            );

            if (result == null || result == 0) {
                stopWatchDog(lockKey, requestId);
            }

        }, interval, interval, TimeUnit.MILLISECONDS);

        RENEW_MAP.put(taskKey, future);
    }

    /**
     * 停止看门狗续期任务
     */
    private void stopWatchDog(String lockKey, String requestId) {
        String taskKey = lockKey + ":" + requestId;
        ScheduledFuture<?> future = RENEW_MAP.remove(taskKey);
        if (future != null) {
            future.cancel(false);
        }
    }

    /**
     * 函数式锁（带返回值）
     * @param lockKey 业务锁名
     * @param supplier 执行的函数
     * @return 执行结果
     */
    public <T> T execute(String lockKey, Supplier<T> supplier) {
        String requestId = lock(lockKey);
        if (requestId == null) {
            throw new RuntimeException("获取锁失败");
        }
        try {
            return supplier.get();
        } finally {
            unlock(lockKey, requestId);
        }
    }

    /**
     * 函数式锁（无返回值）
     * @param lockKey 业务锁名
     * @param runnable 执行的任务
     */
    public void execute(String lockKey, Runnable runnable) {
        String requestId = lock(lockKey);
        if (requestId == null) {
            throw new RuntimeException("获取锁失败");
        }
        try {
            runnable.run();
        } finally {
            unlock(lockKey, requestId);
        }
    }
}
