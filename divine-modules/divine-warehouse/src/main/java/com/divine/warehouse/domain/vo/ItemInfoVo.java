package com.divine.warehouse.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
public class ItemInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 编号
     */
    private String itemNo;

    /**
     * 名称
     */
    private String itemName;

    /**
     * 单位类别
     */
    private String unit;


    /**
     * 备注
     */
    private String remark;

    /**
     * 规格信息
     */
    private List<ItemSkuVo> skuList;
}
