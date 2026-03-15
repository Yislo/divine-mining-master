package com.divine.system.domain.vo;

import com.divine.system.domain.entity.SysNotice;
import com.divine.common.translation.annotation.Translation;
import com.divine.common.translation.constant.TransConstant;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 通知公告视图对象 sys_notice
 *
 * @author Michelle.Chung
 */
@Data
@AutoMapper(target = SysNotice.class)
public class SysNoticeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告类型（1通知 2公告）
     */
    private Integer noticeType;

    /**
     * 消息事件类型(1:库存预警通知,2:车辆进厂通知)
     */
    private Integer eventType;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告状态（0:关闭,1:正常）
     */
    private String status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createBy")
    private String createByName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
