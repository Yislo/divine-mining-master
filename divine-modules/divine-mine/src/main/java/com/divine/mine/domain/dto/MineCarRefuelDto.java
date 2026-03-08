package com.divine.mine.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.mine.domain.entity.MineCarRefuel;
import lombok.Data;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;


/**
 * 车辆加油记录业务对象 mine_car_refuel
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@AutoMapper(target = MineCarRefuel.class, reverseConvertGenerate = false)
public class MineCarRefuelDto {

    /**
     * 主键id(修改时传入)
     */
    @NotNull(message = "主键id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 车辆id
     */
    private Long carId;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 车辆编号
     */
    private String carNo;

    /**
     * 是否外来车辆(0:否,1:是)
     */
    @NotNull(message = "是否外来车辆(0:否,1:是)不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long isExternal;

    /**
     * 加油时里程表(km)
     */
    private Long odometer;

    /**
     * 加油量(L)
     */
    @NotNull(message = "加油量(L)不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long litre;

    /**
     * 加油类型(0:柴油,1:汽油)
     */
    @NotBlank(message = "加油类型(0:柴油,1:汽油)不能为空", groups = {AddGroup.class, EditGroup.class})
    private String refuelType;

    /**
     * 备注
     */
    private String remark;



}
