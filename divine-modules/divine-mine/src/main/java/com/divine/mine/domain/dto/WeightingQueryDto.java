package com.divine.mine.domain.dto;

import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.mine.domain.entity.MineWeighting;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 过磅记录业务对象 mine_weighting
 *
 * @author yisl
 * @date 2026-02-28
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = MineWeighting.class, reverseConvertGenerate = false)
public class WeightingQueryDto extends BasePage {

    /**
     * 过磅单编号
     */
    private String weighingNo;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 商品类型
     */
    private Integer goodsType;

    /**
     * 过磅状态
     */
    private Integer weighingStatus;

    /**
     * 发货单位
     */
    private Long shipMerchantId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;




}
