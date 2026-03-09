package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.entity.Inventory;
import com.divine.warehouse.domain.vo.BoardListVO;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存Mapper接口
 *
 * @author yisl
 * @date 2024-07-19
 */
@Mapper
public interface InventoryMapper extends BaseMapperPlus<Inventory, InventoryVo> {

    Page<BoardListVO> queryItemBoardList(Page<InventoryVo> page, @Param("dto") InventoryDto dto);
    Page<BoardListVO> queryWarehouseBoardList(Page<InventoryVo> page, @Param("dto") InventoryDto dto);

    /**
     * 扣减库存
     * @param warehouseId
     * @param skuId
     * @param storageShelf
     * @param quantity
     * @return
     */
    int subtractStock(Long warehouseId, Long skuId, String storageShelf, Long quantity);
}
