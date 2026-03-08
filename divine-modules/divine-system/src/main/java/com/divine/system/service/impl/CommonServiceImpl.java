package com.divine.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.divine.common.core.constant.RedisKeyConstants;
import com.divine.common.core.domain.vo.OptionVO;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.system.domain.entity.SysConfig;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.system.mapper.SysConfigMapper;
import com.divine.system.service.CommonService;
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
