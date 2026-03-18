package com.divine.mine.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.mine.domain.entity.MineQuality;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 送货质量业务对象 mine_quality
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@AutoMapper(target = MineQuality.class, reverseConvertGenerate = false)
public class MineQualityDto {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
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
     * 过磅单id
     */
    @NotEmpty(message = "过磅单不能为空", groups = { EditGroup.class })
    private List<Long> weightingId;

    /**
     * 送货单位id(多条过磅单的送货单位一致才能作为一条质检单)
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
     * 备注
     */
    private String remark;


}
