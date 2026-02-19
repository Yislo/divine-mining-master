package com.divine.warehouse.domain.vo;

import com.divine.system.domain.vo.SysFileVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 入库单详情视图对象 wms_receipt_order_detail
 *
 * @author yisl
 * @date 2024-07-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptOrderInfoVO {

    //入库单号、仓库、物品名称、规格、入库数量、入库类型、单价、总价、货架、入库状态、操作时间、操作人、备注、操作记录、图片

    private String receiptNo;
    private String warehouse_id;
    private String warehouseName;
    private String storageShelf;
    private String itemId;
    private String itemName;
    private String skuId;
    private String skuName;
    private String quantity;
    private String optType;
    private String unit_price;
    private String total_price;
    private String receiptStatus;
    private String createTime;
    private String createBy;
    private List<SysFileVo> fileList;


}
