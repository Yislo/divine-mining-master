package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.List;

/**
 * 出库单Service业务层处理
 *
 * @author zcc
 * @date 2024-08-01
 */
public interface ShipmentOrderService {

    /**
     * 查询出库单
     */
    ShipmentOrderVo queryById(Long id);

    /**
     * 查询出库单列表
     */
    PageInfoRes<ShipmentOrderVo> queryPageList(ShipmentOrderDto bo, BasePage basePage) ;

    /**
     * 查询出库单列表
     */
    List<ShipmentOrderVo> queryList(ShipmentOrderDto bo) ;

    /**
     * 暂存出库单
     */
    Long insertByBo(ShipmentOrderDto bo) ;

    void validateShipmentOrderNo(String shipmentOrderNo);

    /**
     * 修改出库单
     */
    void updateByBo(ShipmentOrderDto bo) ;

    /**
     * 批量删除出库单
     */
    void deleteById(Long id) ;

    void validateIdBeforeDelete(Long id);

    /**
     * 出库
     * @param bo
     */
    void shipment(ShipmentOrderDto bo);

    Long queryIdByOrderNo(String orderNo);
}
