package com.divine.mine.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.mine.domain.entity.MineCar;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;


/**
 * 车辆信息业务对象 mine_car
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MineCar.class, reverseConvertGenerate = false)
public class MineCarDto extends BaseEntity {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车辆编号
     */
    private String carNo;

    /**
     * 驾驶员电话
     */
    private String driverPhone;

    /**
     * 驾驶员
     */
    private String driver;

    /**
     * 车辆类型
     */
    @NotNull(message = "车辆类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long carType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除(0:未删除,1:已删除)
     */
    private Integer isDel;


}
