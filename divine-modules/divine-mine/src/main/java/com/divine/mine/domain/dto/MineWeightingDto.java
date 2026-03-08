package com.divine.mine.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.mine.domain.entity.MineWeighting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.util.Date;

/**
 * 过磅记录业务对象 mine_weighting
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MineWeighting.class, reverseConvertGenerate = false)
public class MineWeightingDto extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 过磅单编号
     */
    private String weighingNo;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 过磅状态(0:已过磅,1:已回磅)
     */
    private Integer weighingStatus;

    /**
     * 发货单位
     */
    private Long shipMerchantId;

    /**
     * 发货日期
     */
    private Date shipTime;

    /**
     * 发货地址
     */
    private String shipAddress;

    /**
     * 收货单位
     */
    private String deliveryMerchant;

    /**
     * 收货日期
     */
    private Date deliveryTime;

    /**
     * 总重
     */
    private Long totalWeight;

    /**
     * 皮重
     */
    private Long tareWeight;

    /**
     * 净重
     */
    private Long netWeight;

    /**
     * 备注
     */
    private String remark;


}
