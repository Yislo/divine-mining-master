package com.divine.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.divine.common.core.constant.RedisKeyConstants;
import com.divine.common.core.domain.vo.OptionVO;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.system.domain.entity.SysConfig;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.system.mapper.SysConfigMapper;
import com.divine.warehouse.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author yisl
 * @date 2024-08-13
 */
@RequiredArgsConstructor
@Service
public class CommonServiceImpl implements CommonService {

    private final SysConfigMapper configMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String getNo(String type) {
        // 1. 当天日期（yyyyMMdd）
        String dateStr = LocalDate.now().format(DATE_FORMATTER);
        // 2. Redis Key（按前缀 + 按天）
        String redisKey = RedisKeyConstants.NO_KEY + type + ":" + dateStr;
        // 3. Redis 原子自增，过期到当天 23:59:59
        long num = RedisUtils.incrAtomicValue(redisKey, getSecondsUntilMidnight());
        // 4. 可选：限制当日最大流水
        if (num > 999) {
            throw new BusinessException("当日业务单号已达上限");
        }
        // 5. 拼接业务单号
        return type + dateStr + String.format("%03d", num);
    }


    /**
     * 获取距离今天结束（23:59:59）的秒数
     */
    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate()
            .plusDays(1)
            .atStartOfDay();
        return Duration.between(now, midnight).getSeconds();
    }

    @Override
    public List<OptionVO> getOption(String type) {
        return List.of();
    }

    /**
     * 获取配置参数值
     *
     * @param keyName keyName
     * @return SysConfigVo
     */
    @Override
    public SysConfigVo getConfigParam(String keyName) {
        return configMapper.selectVoOne(new LambdaQueryWrapper<>(SysConfig.class)
            .eq(SysConfig::getConfigKey, keyName));
    }
}
