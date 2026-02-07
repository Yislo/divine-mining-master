package com.divine.system.mapper;

import com.divine.system.domain.entity.SysConfig;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 参数配置 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysConfigMapper extends BaseMapperPlus<SysConfig, SysConfigVo> {

}
