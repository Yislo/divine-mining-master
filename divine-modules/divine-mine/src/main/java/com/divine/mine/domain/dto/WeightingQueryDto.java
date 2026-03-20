package com.divine.mine.domain.dto;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.mine.domain.entity.MineWeighting;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;


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
     * 查询可生成质量单的数据(0:否，1:是)
     */
    private Integer isQuality;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;




}
