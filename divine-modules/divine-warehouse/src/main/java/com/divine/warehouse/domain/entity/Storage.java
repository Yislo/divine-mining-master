package com.divine.warehouse.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class Storage {

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;


    /**
     * 货架名称
     */
    private String StorageName;

}
