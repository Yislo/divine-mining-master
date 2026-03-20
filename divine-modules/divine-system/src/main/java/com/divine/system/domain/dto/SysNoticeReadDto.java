package com.divine.system.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.system.domain.entity.SysNoticeRead;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDateTime;

/**
 * 消息已读业务对象 sys_notice_read
 *
 * @author yisl
 * @date 2026-03-12
 */

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysNoticeRead.class, reverseConvertGenerate = false)
public class SysNoticeReadDto extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 通知ID
     */
    @NotNull(message = "通知ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long noticeId;

    /**
     * 接收用户
     */
    @NotNull(message = "接收用户不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 是否已读
     */
    @NotNull(message = "是否已读不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isRead;

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private LocalDateTime readTime;


}
