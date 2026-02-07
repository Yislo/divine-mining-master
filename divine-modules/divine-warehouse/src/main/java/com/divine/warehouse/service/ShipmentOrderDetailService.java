package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 出库单详情Service业务层处理
 *
 * @author zcc
 * @date 2024-08-01
 */
public interface ShipmentOrderDetailService{


    /**
     * 查询出库单详情
     */
    ShipmentOrderDetailVo queryById(Long id);

    /**
     * 查询出库单详情列表
     */
    PageInfoRes<ShipmentOrderDetailVo> queryPageList(ShipmentOrderDetailDto bo, BasePage basePage);

    /**
     * 查询出库单详情列表
     */
    List<ShipmentOrderDetailVo> queryList(ShipmentOrderDetailDto bo) ;

    /**
     * 新增出库单详情
     */
    void insertByBo(ShipmentOrderDetailDto bo);

    /**
     * 修改出库单详情
     */
    void updateByBo(ShipmentOrderDetailDto bo);

    /**
     * 批量删除出库单详情
     */
    void deleteByIds(Collection<Long> ids);

    void saveDetails(List<ShipmentOrderDetail> list);

    List<ShipmentOrderDetailVo> queryByShipmentOrderId(Long shipmentOrderId);
}
