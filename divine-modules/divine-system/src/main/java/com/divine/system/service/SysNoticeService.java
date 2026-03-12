package com.divine.system.service;

import com.divine.common.core.domain.Result;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysNoticeDto;
import com.divine.system.domain.vo.MyNoticeVo;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.List;

/**
 * 公告 服务层实现
 *
 * @author Lion Li
 */
public interface SysNoticeService {


    /**
     * 发送消息通知
     *
     * @param dto
     */
    void sendMessage(SysNoticeDto dto);


    PageInfoRes<SysNoticeVo> selectPageNoticeList(SysNoticeDto notice, BasePage basePage);

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    SysNoticeVo selectNoticeById(Long noticeId);

    /**
     * 获取我的通知消息
     *
     * @param basePage
     * @return
     */
    PageInfoRes<MyNoticeVo> getMyNotice(BasePage basePage);

    /**
     * 已读
     *
     * @param id
     */
    void read(List<Long> id);

    /**
     * 获取为未读消息数量
     *
     * @return
     */
    Long getUnreadCont();

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    List<SysNoticeVo> selectNoticeList(SysNoticeDto notice);

    /**
     * 新增公告
     *
     * @param dto 公告信息
     * @return 结果
     */
    int insertNotice(SysNoticeDto dto);

    /**
     * 修改公告
     *
     * @param dto 公告信息
     * @return 结果
     */
    int updateNotice(SysNoticeDto dto);

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    int deleteNoticeById(Long noticeId);

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    int deleteNoticeByIds(Long[] noticeIds);

}
