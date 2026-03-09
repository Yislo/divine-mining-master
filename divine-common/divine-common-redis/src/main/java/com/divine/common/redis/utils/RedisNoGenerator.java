package com.divine.common.redis.utils;

import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.utils.GenerateNoUtil;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RedisNoGenerator implements GenerateNoUtil {


    /**
     * 编号后缀长度
     */
    private static final String SUFFIX = "%03d";

    /**
     * 获取业务编号
     * 根据具体业务情况来决定编号长度
     *
     * @param noType
     * @return
     */
    @Override
    public String getBizNo(NoTypeEnum noType) {
        String code = noType.getCode();
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = "no:" + code + ":" + dateStr;
        long num = RedisUtils.incrAtomicValue(redisKey, RedisUtils.getSecondsUntilMidnight());
        return code + dateStr + String.format(SUFFIX, num);
    }



}
