package com.divine.mine.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.mine.domain.entity.MineAccessRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import io.github.linpeilie.annotations.AutoMapper;

import java.time.LocalDateTime;

/**
 * 车辆出入厂记录业务对象 mine_access_record
 *
 * @author yisl
 * @date 2026-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = MineAccessRecord.class, reverseConvertGenerate = false)
public class MineAccessRecordDto extends BaseEntity {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = { AddGroup.class, EditGroup.class })
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
