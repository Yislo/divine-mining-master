package com.divine.warehouse.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ItemSkuMapVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long skuId;
    private ItemVo item;
    private ItemSkuVo itemSku;
    /**
     * 货架信息
     */
    private List<String> storageShelf;
}
