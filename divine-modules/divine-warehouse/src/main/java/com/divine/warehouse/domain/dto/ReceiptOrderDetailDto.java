package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.warehouse.domain.entity.Inventory;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单详情业务对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AutoMappers({
    @AutoMapper(target = ReceiptOrderDetail.class, reverseConvertGenerate = false),
    @AutoMapper(target = Inventory.class, reverseConvertGenerate = false)
})
public class ReceiptOrderDetailDto extends BaseOrderDetailDto {

    /**
     * 入库单id
     */
    @NotNull(message = "入库单id", groups = { AddGroup.class, EditGroup.class })
    private Long receiptId;

}
