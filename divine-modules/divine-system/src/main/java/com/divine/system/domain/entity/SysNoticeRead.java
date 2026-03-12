package com.divine.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import java.io.Serial;

/**
 * 消息已读对象 sys_notice_read
 *
 * @author yisl
 * @date 2026-03-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_notice_read")
public class SysNoticeRead implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 通知ID
     */
    private Long noticeId;
    /**
     * 接收用户
     */
    private Long userId;
    /**
     * 是否已读
     */
    private Integer isRead;
    /**
     * 已读时间
     */
    private Date readTime;

}
