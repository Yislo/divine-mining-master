package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.InventoryHistoryDto;
import com.divine.warehouse.domain.entity.InventoryHistory;
import com.divine.warehouse.domain.vo.InventoryHistoryVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存记录Mapper接口
 *
 * @author zcc
 * @date 2024-07-22
 */
@Mapper
public interface InventoryHistoryMapper extends BaseMapperPlus<InventoryHistory, InventoryHistoryVo> {

    Page<InventoryHistoryVo> selectVoPageByBo(Page<Object> page, @Param("bo") InventoryHistoryDto bo);
}
