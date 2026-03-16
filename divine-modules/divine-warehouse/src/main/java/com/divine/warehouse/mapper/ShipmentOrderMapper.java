package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import com.divine.warehouse.domain.vo.ShipmentImportVO;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库单Mapper接口
 *
 * @author yisl
 * @date 2024-08-01
 */
@Mapper
public interface ShipmentOrderMapper extends BaseMapperPlus<ShipmentOrder, ShipmentOrderVo> {

    /**
     * 查询导出出库单数据
     *
     * @param dto
     * @return
     */
    List<ShipmentImportVO> selectShipmentList(@Param("dto")ShipmentOrderDto dto);


}
