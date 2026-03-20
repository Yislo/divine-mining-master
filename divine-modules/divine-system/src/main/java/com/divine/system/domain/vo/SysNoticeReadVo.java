package com.divine.system.domain.vo;

import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.system.domain.entity.SysNoticeRead;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;

/**
 * 消息已读视图对象 sys_notice_read
 *
 * @author yisl
 * @date 2026-03-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysNoticeRead.class)
public class SysNoticeReadVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 通知ID
     */
    @ExcelProperty(value = "通知ID")
    private Long noticeId;

    /**
     * 接收用户
     */
    @ExcelProperty(value = "接收用户")
    private Long userId;

    /**
     * 是否已读
     */
    @ExcelProperty(value = "是否已读")
    private Long isRead;

    /**
     *
     */
    @ExcelProperty(value = "")
    private LocalDateTime readTime;


}
