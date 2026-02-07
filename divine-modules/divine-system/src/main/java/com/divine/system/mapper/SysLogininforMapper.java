package com.divine.system.mapper;

import com.divine.system.domain.entity.SysLogininfor;
import com.divine.system.domain.vo.SysLogininforVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统访问日志情况信息 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysLogininforMapper extends BaseMapperPlus<SysLogininfor, SysLogininforVo> {

}
