package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单详情Mapper接口
 *
 * @author zcc
 * @date 2024-08-01
 */
@Mapper
public interface ShipmentOrderDetailMapper extends BaseMapperPlus<ShipmentOrderDetail, ShipmentOrderDetailVo> {

}
