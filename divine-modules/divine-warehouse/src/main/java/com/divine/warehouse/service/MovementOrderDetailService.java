package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.MovementOrderDetailDto;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.vo.MoveDetailVO;
import com.divine.warehouse.domain.vo.MovementOrderDetailVO;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 库存移动详情Service业务层处理
 *
 * @author yisl
 * @date 2024-08-09
 */
public interface MovementOrderDetailService{

    /**
     * 查询库存移动详情
     */
    MovementOrderDetailVO queryById(Long id);

    /**
     * 查询库存移动详情列表
     */
    PageInfoRes<MoveDetailVO> queryPageList(Long moveId, BasePage basePage);

    /**
     * 查询库存移动详情列表
     */
    List<MovementOrderDetailVO> queryList(MovementOrderDetailDto dto);

    /**
     * 新增库存移动详情
     */
    void insertByBo(MovementOrderDetailDto dto);

    /**
     * 修改库存移动详情
     */
    void updateByBo(MovementOrderDetailDto dto);

    /**
     * 批量删除库存移动详情
     */
    void deleteByIds(Collection<Long> ids);

    void saveDetails(List<MovementOrderDetail> list);

    /**
     * 根据移库单id查询移库单详情
     * @param movementOrderId
     * @return
     */
    List<MovementOrderDetailVO> queryByMovementOrderId(Long movementOrderId);
}
