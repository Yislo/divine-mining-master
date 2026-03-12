package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.system.domain.dto.SysNoticeRuleDto;
import com.divine.system.domain.vo.SysNoticeRuleVo;

import java.util.Collection;
import java.util.List;

/**
 * 消息推送规则Service业务层处理
 *
 * @author yisl
 * @date 2026-03-12
 */
public interface SysNoticeRuleService {

    /**
     * 查询消息推送规则列表
     */
    PageInfoRes<SysNoticeRuleVo> queryPageList(SysNoticeRuleDto dto, BasePage basePage);

    /**
     * 新增消息推送规则
     */
    void insertByBo(SysNoticeRuleDto dto);
    /**
     * 修改消息推送规则
     */
    void updateByBo(SysNoticeRuleDto dto);

    /**
     * 批量删除消息推送规则
     */
    void deleteByIds(Collection<Long> ids);
}
