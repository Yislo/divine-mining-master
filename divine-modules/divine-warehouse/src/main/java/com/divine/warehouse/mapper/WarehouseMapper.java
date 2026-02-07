package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.WarehouseVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WarehouseMapper extends BaseMapperPlus<Warehouse, WarehouseVo> {
}
