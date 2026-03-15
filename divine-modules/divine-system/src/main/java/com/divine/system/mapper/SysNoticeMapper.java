package com.divine.system.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.system.domain.entity.SysNotice;
import com.divine.system.domain.vo.MyNoticeVo;
import com.divine.system.domain.vo.SysNoticeVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告表 数据层
 *
 * @author Lion Li
 */
@Mapper
public interface SysNoticeMapper extends BaseMapperPlus<SysNotice, SysNoticeVo> {


    /**
     * 获取我的消息
     * @param page
     * @param userId
     * @return
     */
    Page<MyNoticeVo> getMyNotice(Page<MyNoticeVo> page, Long userId,Integer noticeType);

    /**
     * 获取未读消息数量
     * @param userId
     * @return
     */
    Long getUnreadCont(Long userId);

}
