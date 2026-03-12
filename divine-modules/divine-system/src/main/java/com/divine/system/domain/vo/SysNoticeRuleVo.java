package com.divine.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.divine.system.domain.entity.SysNoticeRule;
import lombok.Data;
import io.github.linpeilie.annotations.AutoMapper;

import java.io.Serializable;
import java.io.Serial;

/**
 * 消息推送规则视图对象 sys_notice_rule
 *
 * @author yisl
 * @date 2026-03-12
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = SysNoticeRule.class)
public class SysNoticeRuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 消息事件类型(1:库存预警通知,2:车辆进厂通知)
     */
    @ExcelProperty(value = "消息事件类型")
    private Integer eventType;

    /**
     * 1用户 2岗位
     */
    @ExcelProperty(value = "1用户 2岗位")
    private Integer targetType;

    /**
     * 用户ID/岗位ID
     */
    @ExcelProperty(value = "用户ID/岗位ID")
    private Long targetId;

    /**
     * 用户名/岗位名称
     */
    @ExcelProperty(value = "用户名/岗位名称")
    private String targetName;


}
