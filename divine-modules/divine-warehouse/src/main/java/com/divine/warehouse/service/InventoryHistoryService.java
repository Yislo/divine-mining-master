package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.BaseOrderDto;
import com.divine.warehouse.domain.dto.InventoryHistoryDto;
import com.divine.warehouse.domain.vo.InventoryHistoryVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 库存记录Service业务层处理
 *
 * @author zcc
 * @date 2024-07-22
 */
public interface InventoryHistoryService {


    void saveInventoryHistory(BaseOrderDto<? extends BaseOrderDetailDto> bo, Integer orderType, Boolean isAdd);

    /**
     * 查询库存记录
     */
    InventoryHistoryVo queryById(Long id);

    /**
     * 查询库存记录列表
     */
    PageInfoRes<InventoryHistoryVo> queryPageList(InventoryHistoryDto bo, BasePage basePage);

    /**
     * 查询库存记录列表
     */
    List<InventoryHistoryVo> queryList(InventoryHistoryDto bo);

    /**
     * 新增库存记录
     */
    void insertByBo(InventoryHistoryDto bo);

    /**
     * 修改库存记录
     */
    void updateByBo(InventoryHistoryDto bo);

    /**
     * 批量删除库存记录
     */
    void deleteByIds(Collection<Long> ids);
}
