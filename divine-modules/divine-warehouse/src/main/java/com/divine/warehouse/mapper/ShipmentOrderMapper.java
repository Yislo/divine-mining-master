package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ShipmentOrder;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单Mapper接口
 *
 * @author zcc
 * @date 2024-08-01
 */
@Mapper
public interface ShipmentOrderMapper extends BaseMapperPlus<ShipmentOrder, ShipmentOrderVo> {

}
