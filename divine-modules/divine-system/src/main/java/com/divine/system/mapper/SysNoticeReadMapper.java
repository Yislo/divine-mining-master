package com.divine.system.mapper;

import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import com.divine.system.domain.entity.SysNoticeRead;
import com.divine.system.domain.vo.SysNoticeReadVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息已读Mapper接口
 *
 * @author yisl
 * @date 2026-03-12
 */
@Mapper
public interface SysNoticeReadMapper extends BaseMapperPlus<SysNoticeRead, SysNoticeReadVo> {

}
