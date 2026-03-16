package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.vo.ShipmentImportVO;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 出库单Service业务层处理
 *
 * @author yisl
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
    PageInfoRes<ShipmentOrderVo> queryPageList(ShipmentOrderDto dto, BasePage basePage) ;

    /**
     * 查询出库单列表
     */
    void export(ShipmentOrderDto dto, HttpServletResponse response) ;

    /**
     * 暂存出库单
     */
    Long insertByBo(ShipmentOrderDto dto) ;


    /**
     * 修改出库单
     */
    void updateByBo(ShipmentOrderDto dto) ;

    /**
     * 批量删除出库单
     */
    void deleteById(Long id) ;

    void validateIdBeforeDelete(Long id);

    /**
     * 出库
     * @param dto
     */
    void shipment(ShipmentOrderDto dto);

    Long queryIdByOrderNo(String orderNo);
}
