package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysOssConfigDto;
import com.divine.system.domain.vo.SysOssConfigVo;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;

/**
 * 对象存储配置Service业务层处理
 *
 * @author Lion Li
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
public interface SysOssConfigService {

    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    void init();

    SysOssConfigVo queryById(Long ossConfigId);

    PageInfoRes<SysOssConfigVo> queryPageList(SysOssConfigDto bo, BasePage basePage);

    Boolean insertByBo(SysOssConfigDto bo);

    Boolean updateByBo(SysOssConfigDto bo);

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 启用禁用状态
     */
    int updateOssConfigStatus(SysOssConfigDto bo);

}
