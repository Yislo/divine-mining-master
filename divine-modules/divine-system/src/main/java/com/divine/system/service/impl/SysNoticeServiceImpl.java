package com.divine.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.satoken.utils.LoginHelper;
import com.divine.system.domain.dto.NoticeReadDto;
import com.divine.system.domain.dto.SysNoticeDto;
import com.divine.system.domain.entity.SysNotice;
import com.divine.system.domain.vo.MyNoticeVo;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.system.mapper.SysNoticeMapper;
import com.divine.system.service.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 公告 服务层实现
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeMapper noticeMapper;
    private final NoticeReadService noticeReadService;

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
    public PageInfoRes<MyNoticeVo> getMyNotice(BasePage basePage) {
        Long userId = LoginHelper.getUserId();
        Page<MyNoticeVo> res = new Page<>(basePage.getPageNum(), basePage.getPageSize());
        res = noticeMapper.getMyNotice(res, userId);
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
        // 组装数据新增
        Date date = new Date();
        List<NoticeReadDto> list = ids.stream().map(id ->
            NoticeReadDto.builder()
                .noticeId(id)
                .userId(userId)
                .readTime(date)
                .build()).toList();
        // 批量已读
        noticeReadService.insertByDto(list);
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
        // 获取所有通知消息
        // 获取已读通知消息
        // 计算未读消息
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
    public int insertNotice(SysNoticeDto dto) {
        SysNotice notice = MapstructUtils.convert(dto, SysNotice.class);
        return noticeMapper.insert(notice);
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
