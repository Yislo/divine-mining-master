package com.divine.system.mapper;

import com.divine.system.domain.entity.SysNotice;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysNoticeMapper extends BaseMapperPlus<SysNotice, SysNoticeVo> {

}
