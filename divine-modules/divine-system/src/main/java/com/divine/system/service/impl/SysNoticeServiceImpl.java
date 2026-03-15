package com.divine.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.satoken.utils.LoginHelper;
import com.divine.system.domain.dto.MyNoticeDto;
import com.divine.system.domain.dto.SysNoticeReadDto;
import com.divine.system.domain.dto.SysNoticeDto;
import com.divine.system.domain.entity.SysNoticeRead;
import com.divine.system.domain.entity.SysNoticeRule;
import com.divine.system.domain.entity.SysNotice;
import com.divine.system.domain.entity.SysUser;
import com.divine.system.domain.vo.MyNoticeVo;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.system.domain.vo.SysUserVo;
import com.divine.system.mapper.SysNoticeMapper;
import com.divine.system.mapper.SysNoticeReadMapper;
import com.divine.system.mapper.SysNoticeRuleMapper;
import com.divine.system.mapper.SysUserMapper;
import com.divine.system.service.SysNoticeService;
import com.divine.system.service.SysPostService;
import com.divine.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 公告 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;
    private final SysNoticeReadMapper noticeReadMapper;
    private final SysNoticeRuleMapper noticeRuleMapper;
    private final SysPostService postService;
    private final SysUserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMessage(SysNoticeDto dto) {
        // 保存通知内容
        SysNotice sysNotice = BeanUtil.copyProperties(dto, SysNotice.class);
        noticeMapper.insert(sysNotice);
        // 获取消息时间类型
        Integer eventType = dto.getEventType();
        // 查询该事件的消息规则
        List<SysNoticeRule> ruleList = noticeRuleMapper.selectList(new LambdaQueryWrapper<>(SysNoticeRule.class)
            .eq(SysNoticeRule::getEventType, eventType));
        // 不配置 默认发送给所有人
        List<Long> sendUserId;
        if (ruleList.isEmpty()) {
            List<SysUser> sysUsers = userMapper.selectList();
            sendUserId = sysUsers.stream().map(SysUser::getUserId).toList();
        } else {
            // 获取收件人id
            sendUserId = getSendUserId(ruleList);
        }
        if (CollUtil.isEmpty(sendUserId)) {
            return;
        }
        // 推送指定用户消息 todo 如果用户量过大，该业务需要优化
        List<SysNoticeRead> readList = sendUserId.stream().map(userId -> {
            SysNoticeRead sysNoticeRead = new SysNoticeRead();
            sysNoticeRead.setNoticeId(sysNotice.getNoticeId());
            sysNoticeRead.setUserId(userId);
            return sysNoticeRead;

        }).toList();
        noticeReadMapper.insertBatch(readList);
    }

    /**
     * 获取消息推送用户id
     *
     * @param ruleList
     * @return
     */
    private List<Long> getSendUserId(List<SysNoticeRule> ruleList) {
        // 使用 Set 存储，自动去重，且避免不可变集合的 addAll 报错
        Set<Long> finalUserIds = new HashSet<>();
        // 1. 提取并加入直接指定的用户 ID
        ruleList.stream()
            .filter(r -> r.getTargetType() == 1)
            .map(r -> Long.valueOf(r.getTargetId()))
            .forEach(finalUserIds::add);
        // 2. 提取岗位 ID 集合
        List<Long> postIds = ruleList.stream()
            .filter(r -> r.getTargetType() == 2)
            .map(r -> Long.valueOf(r.getTargetId()))
            .distinct()
            .toList();
        // 3. 根据岗位获取用户并加入 Set
        if (CollUtil.isNotEmpty(postIds)) {
            List<SysUserVo> postUserList = postService.getPostUser(postIds);
            if (CollUtil.isNotEmpty(postUserList)) {
                postUserList.stream()
                    .map(SysUserVo::getUserId)
                    .forEach(finalUserIds::add);
            }
        }
        return new ArrayList<>(finalUserIds);
    }


    @Override
    public PageInfoRes<SysNoticeVo> selectPageNoticeList(SysNoticeDto notice, BasePage basePage) {
        LambdaQueryWrapper<SysNotice> lqw = buildQueryWrapper(notice);
        Page<SysNoticeVo> page = noticeMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(page);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNoticeVo selectNoticeById(Long noticeId) {
        return noticeMapper.selectVoById(noticeId);
    }

    /**
     * 获取我的消息
     *
     * @param basePage
     * @return
     */
    @Override
    public PageInfoRes<MyNoticeVo> getMyNotice(MyNoticeDto dto) {
        Long userId = LoginHelper.getUserId();
        Page<MyNoticeVo> res = new Page<>(dto.getPageNum(), dto.getPageSize());
        res = noticeMapper.getMyNotice(res, userId,dto.getNoticeType());
        return PageInfoRes.build(res);
    }

    /**
     * 已读
     *
     * @param ids
     */
    @Override
    public void read(List<Long> ids) {
        Long userId = LoginHelper.getUserId();
        List<SysNoticeRead> readList = noticeReadMapper.selectList(new LambdaQueryWrapper<>(SysNoticeRead.class)
            .eq(SysNoticeRead::getUserId, userId)
            .in(SysNoticeRead::getNoticeId, ids));
        if (readList.isEmpty()){
            return;
        }
        // 组装数据新增
        readList.forEach(read -> {
            read.setIsRead(1);
            read.setReadTime(new Date());
        });
        // 批量已读
        noticeReadMapper.updateBatchById(readList);
    }

    /**
     * 一键已读
     */
    @Override
    public void oneClickRead() {
        Long userId = LoginHelper.getUserId();
        List<SysNoticeRead> readList = noticeReadMapper.selectList(new LambdaQueryWrapper<>(SysNoticeRead.class)
            .eq(SysNoticeRead::getUserId, userId));
        if (readList.isEmpty()) {
            return;
        }
        readList.forEach(r -> r.setIsRead(1));
        // 批量已读
        noticeReadMapper.updateBatchById(readList);
    }

    /**
     * 获取未读消息数量
     *
     * @return
     */
    @Override
    public Long getUnreadCont() {
        // 获取当前登录人
        Long userId = LoginHelper.getUserId();
        return noticeMapper.getUnreadCont(userId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNoticeVo> selectNoticeList(SysNoticeDto notice) {
        LambdaQueryWrapper<SysNotice> lqw = buildQueryWrapper(notice);
        return noticeMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysNotice> buildQueryWrapper(SysNoticeDto dto) {
        LambdaQueryWrapper<SysNotice> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(dto.getNoticeTitle()), SysNotice::getNoticeTitle, dto.getNoticeTitle());
        lqw.eq(ObjUtil.isNotNull(dto.getNoticeType()), SysNotice::getNoticeType, dto.getNoticeType());
        lqw.eq(StringUtils.isNotBlank(dto.getCreateBy()), SysNotice::getCreateBy, dto.getCreateBy());
        lqw.orderByAsc(SysNotice::getNoticeId);
        return lqw;
    }

    /**
     * 新增公告
     *
     * @param dto 公告信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertNotice(SysNoticeDto dto) {
        sendMessage(dto);
        return 1;
    }

    /**
     * 修改公告
     *
     * @param dto 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNoticeDto dto) {
        SysNotice notice = MapstructUtils.convert(dto, SysNotice.class);
        return noticeMapper.updateById(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId) {
        return noticeMapper.deleteById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return noticeMapper.deleteBatchIds(Arrays.asList(noticeIds));
    }
}
