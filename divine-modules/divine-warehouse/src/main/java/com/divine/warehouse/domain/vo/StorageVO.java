package com.divine.warehouse.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class StorageVO {

    /**
     * id
     */
    private Long id;


    /**
     * 货架名称
     */
    private String StorageName;

}
