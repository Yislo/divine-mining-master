package com.divine.system.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.system.domain.entity.SysNoticeRule;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;


/**
 * 消息推送规则业务对象 sys_notice_rule
 *
 * @author yisl
 * @date 2026-03-12
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysNoticeRule.class, reverseConvertGenerate = false)
public class SysNoticeRuleDto extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 消息事件类型(1:库存预警通知,2:车辆进厂通知)
     */
    @NotNull(message = "消息事件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer eventType;

    /**
     * 1用户 2岗位
     */
    @NotNull(message = "1用户 2岗位不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetType;

    /**
     * 用户ID/岗位ID
     */
    @NotNull(message = "用户ID/岗位ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long targetId;


}
