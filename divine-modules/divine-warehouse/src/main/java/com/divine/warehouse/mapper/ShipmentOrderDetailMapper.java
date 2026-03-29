package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.warehouse.domain.dto.ShipmentInfoDto;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ReceiptInfoVO;
import com.divine.warehouse.domain.vo.ShipmentDetailVO;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVO;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库单详情Mapper接口
 *
 * @author yisl
 * @date 2024-08-01
 */
@Mapper
public interface ShipmentOrderDetailMapper extends BaseMapperPlus<ShipmentOrderDetail, ShipmentOrderDetailVO> {

    /**
     * 查询出库单详情列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ShipmentDetailVO> getShipmentDetailList(IPage<ReceiptInfoVO> page, ShipmentInfoDto dto);

}
