package com.divine.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.system.domain.dto.SysNoticeRuleDto;
import com.divine.system.domain.entity.SysNoticeRule;
import com.divine.system.domain.vo.SysNoticeRuleVo;
import com.divine.system.domain.vo.SysPostVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.mapper.SysNoticeRuleMapper;
import com.divine.system.service.SysNoticeRuleService;
import com.divine.system.service.SysPostService;
import com.divine.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 消息推送规则Service业务层处理
 *
 * @author yisl
 * @date 2026-03-12
 */
@RequiredArgsConstructor
@Service
public class SysNoticeRuleServiceImpl implements SysNoticeRuleService {

    private final SysNoticeRuleMapper noticeRuleMapper;
    private final SysUserService userService;
    private final SysPostService postService;

    /**
     * 查询消息推送规则列表
     */
    @Override
    public PageInfoRes<SysNoticeRuleVo> queryPageList(SysNoticeRuleDto dto, BasePage basePage) {
        LambdaQueryWrapper<SysNoticeRule> lqw = buildQueryWrapper(dto);
        Page<SysNoticeRuleVo> result = noticeRuleMapper.selectVoPage(basePage.build(), lqw);
        // 包装用户名称/岗位名称
        result.getRecords().forEach(r -> {
            if (r.getTargetType() == 1) {
                SysUserVo sysUserVo = userService.selectUserById(r.getTargetId());
                r.setTargetName(ObjUtil.isNull(sysUserVo) ? "-" : sysUserVo.getNickName());
            }
            if (r.getTargetType() == 2) {
                SysPostVo post = postService.selectPostById(r.getTargetId());
                r.setTargetName(ObjUtil.isNull(post) ? "-" : post.getPostName());
            }
        });
        return PageInfoRes.build(result);
    }

    private LambdaQueryWrapper<SysNoticeRule> buildQueryWrapper(SysNoticeRuleDto dto) {
        LambdaQueryWrapper<SysNoticeRule> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getEventType() != null, SysNoticeRule::getEventType, dto.getEventType());
        lqw.eq(dto.getTargetType() != null, SysNoticeRule::getTargetType, dto.getTargetType());
        lqw.eq(dto.getTargetId() != null, SysNoticeRule::getTargetId, dto.getTargetId());
        return lqw;
    }

    /**
     * 新增消息推送规则
     */
    @Override
    public void insertByBo(SysNoticeRuleDto dto) {
        SysNoticeRule add = MapstructUtils.convert(dto, SysNoticeRule.class);
        noticeRuleMapper.insert(add);
    }

    /**
     * 修改消息推送规则
     */
    @Override
    public void updateByBo(SysNoticeRuleDto dto) {
        SysNoticeRule update = MapstructUtils.convert(dto, SysNoticeRule.class);
        noticeRuleMapper.updateById(update);
    }

    /**
     * 批量删除消息推送规则
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        noticeRuleMapper.deleteBatchIds(ids);
    }
}
