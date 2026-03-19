package com.divine.mine.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.divine.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import java.io.Serial;

/**
 * 送货质量对象 mine_quality
 *
 * @author yisl
 * @date 2026-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("mine_quality")
public class MineQuality extends BaseEntity {

    @Serial
    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 质量编号
     */
    private String qualityNo;
    /**
     * 质量状态
     */
    private Integer qualityStatus;
    /**
     * 送货单位id
     */
    private Long shipMerchantId;
    /**
     * 水份
     */
    private BigDecimal moisture;
    /**
     * 氧化铜品位
     */
    private BigDecimal cuoRatio;
    /**
     * 酸耗
     */
    private BigDecimal acidDemand;
    /**
     * 酸耗
     */
    private String remark;
    /**
     * 是否删除(0:未删除,1:已删除)
     */
    private Integer isDel;

}
