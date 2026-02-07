package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.MovementOrderDetailDto;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.vo.MovementOrderDetailVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 库存移动详情Service业务层处理
 *
 * @author zcc
 * @date 2024-08-09
 */
public interface MovementOrderDetailService{

    /**
     * 查询库存移动详情
     */
    MovementOrderDetailVo queryById(Long id);

    /**
     * 查询库存移动详情列表
     */
    PageInfoRes<MovementOrderDetailVo> queryPageList(MovementOrderDetailDto bo, BasePage basePage);

    /**
     * 查询库存移动详情列表
     */
    List<MovementOrderDetailVo> queryList(MovementOrderDetailDto bo);

    /**
     * 新增库存移动详情
     */
    void insertByBo(MovementOrderDetailDto bo);

    /**
     * 修改库存移动详情
     */
    void updateByBo(MovementOrderDetailDto bo);

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
    List<MovementOrderDetailVo> queryByMovementOrderId(Long movementOrderId);
}
