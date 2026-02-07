package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.MovementOrderDto;
import com.divine.warehouse.domain.vo.MovementOrderVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 移库单Service业务层处理
 *
 * @author zcc
 * @date 2024-08-09
 */
public interface MovementOrderService {

    /**
     * 查询移库单
     */
    MovementOrderVo queryById(Long id);

    /**
     * 查询移库单列表
     */
    PageInfoRes<MovementOrderVo> queryPageList(MovementOrderDto bo, BasePage basePage);

    /**
     * 查询移库单列表
     */
    List<MovementOrderVo> queryList(MovementOrderDto bo);

    /**
     * 新增移库单
     */
    void insertByBo(MovementOrderDto bo);

    /**
     * 修改移库单
     */
    void updateByBo(MovementOrderDto bo);

    /**
     * 删除移库单
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除移库单
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 移库
     * @param bo
     */
    void move(MovementOrderDto bo);

}
