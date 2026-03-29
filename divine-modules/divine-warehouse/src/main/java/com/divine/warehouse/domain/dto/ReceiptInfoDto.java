package com.divine.warehouse.domain.dto;

import com.divine.common.core.validate.AddGroup;
import com.divine.common.core.validate.EditGroup;
import com.divine.common.mybatis.core.page.BasePage;
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
public class ReceiptInfoDto extends BasePage {

    /**
     * 入库单id
     */
    @NotNull(message = "入库单id", groups = { AddGroup.class, EditGroup.class })
    private Long receiptId;

    /**
     * 物品(物品名称和物品编号通用搜索字段)
     */
    private String item;

    /**
     * 规格(规格名称和规格编号通用搜索字段)
     */
    private String sku;

}
