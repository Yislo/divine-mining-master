package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.entity.Inventory;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存Mapper接口
 *
 * @author zcc
 * @date 2024-07-19
 */
@Mapper
public interface InventoryMapper extends BaseMapperPlus<Inventory, InventoryVo> {

    Page<InventoryVo> queryItemBoardList(Page<InventoryVo> page, @Param("bo") InventoryDto bo);
    Page<InventoryVo> queryWarehouseBoardList(Page<InventoryVo> page, @Param("bo") InventoryDto bo);

}
