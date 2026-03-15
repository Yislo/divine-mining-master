package com.divine.system.domain.dto;

import com.divine.common.mybatis.core.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置业务对象 sys_config
 *
 * @author Michelle.Chung
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MyNoticeDto extends BasePage {

    /**
     * 消息类型
     */
    private Integer noticeType;

}
