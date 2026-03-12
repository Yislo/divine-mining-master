package com.divine.system.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serial;
import java.io.Serializable;

/**
 * 消息推送规则对象 sys_notice_rule
 *
 * @author yisl
 * @date 2026-03-12
 */
@Data
@TableName("sys_notice_rule")
public class SysNoticeRule implements Serializable {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 消息事件类型(1:库存预警通知,2:车辆进厂通知)
     */
    private Integer eventType;
    /**
     * 1用户 2岗位
     */
    private Integer targetType;
    /**
     * 用户ID/岗位ID
     */
    private Integer targetId;

}
