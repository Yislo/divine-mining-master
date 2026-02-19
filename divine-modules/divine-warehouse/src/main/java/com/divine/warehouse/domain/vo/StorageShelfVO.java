package com.divine.warehouse.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/12 19:51
 */
@Data
public class StorageShelfVO {

    @Schema(description = "id(删除货架时传这个id)")
    private Long id;

    @Schema(description = "库存数量")
    private Long quantity;

    @Schema(description = "货架")
    private String storageShelf;

}
