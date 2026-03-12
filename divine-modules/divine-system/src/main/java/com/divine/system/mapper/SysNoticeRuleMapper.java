package com.divine.system.mapper;

import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import com.divine.system.domain.entity.SysNoticeRule;
import com.divine.system.domain.vo.SysNoticeRuleVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息推送规则Mapper接口
 *
 * @author yisl
 * @date 2026-03-12
 */
@Mapper
public interface SysNoticeRuleMapper extends BaseMapperPlus<SysNoticeRule, SysNoticeRuleVo> {

}
