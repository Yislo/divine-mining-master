package com.divine.system.mapper;

import com.divine.system.domain.entity.SysOss;
import com.divine.system.domain.vo.SysOssVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysOssMapper extends BaseMapperPlus<SysOss, SysOssVo> {
}
