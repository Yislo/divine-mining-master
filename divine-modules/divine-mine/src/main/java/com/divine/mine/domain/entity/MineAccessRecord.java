package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import java.io.Serial;

/**
 * 车辆出入厂记录对象 mine_access_record
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mine_access_record")
public class MineAccessRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    private Long id;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 进厂时间
     */
    private LocalDateTime enterTime;
    /**
     * 出厂时间
     */
    private LocalDateTime exitTime;
    /**
     * 所属单位
     */
    private Long merchantId;
    /**
     * 进厂类型(0:其他,1:送货,2:外来,3:空车)
     */
    private Long entryType;
    /**
     * 状态
     */
    private Long status;
    /**
     * 备注
     */
    private String remark;

}
