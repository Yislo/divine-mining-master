package com.divine.system.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息已读视图对象 sys_notice_read
 *
 * @author yisl
 * @date 2026-03-12
 */
@Data
public class SysNoticeUnreadVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * userId
     */
    private Long userId;

    /**
     * 未读消息数量
     */
    private Long unreadCount;



}
