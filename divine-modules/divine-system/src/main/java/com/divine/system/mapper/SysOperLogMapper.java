package com.divine.system.mapper;

import com.divine.system.domain.entity.SysOperLog;
import com.divine.system.domain.vo.SysOperLogVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLog, SysOperLogVo> {

}
