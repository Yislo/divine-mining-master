package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 库存Service业务层处理
 *
 * @author zcc
 * @date 2024-07-19
 */
public interface InventoryService {

    /**
     * 查询库存
     */
    InventoryVo queryById(Long id);

    /**
     * 查询库存列表
     */
    List<InventoryVo> queryList(InventoryDto bo);


    /**
     * 新增库存
     */
    void insertByBo(InventoryDto bo);

    /**
     * 修改库存
     */
    void updateByBo(InventoryDto bo);

    /**
     * 批量删除库存
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 校验规格是否有库存
     *
     * @param skuIds
     * @return
     */
    boolean existsBySkuIds(@NotEmpty Collection<Long> skuIds);

    PageInfoRes<InventoryVo> queryWarehouseBoardList(InventoryDto bo, BasePage basePage);

    PageInfoRes<InventoryVo> queryItemBoardList(InventoryDto bo, BasePage basePage);

    void updateInventory(List<CheckOrderDetailDto> details);

    @Transactional
    void add(List<? extends BaseOrderDetailDto> details);

    /**
     * 扣减库存
     *
     * @param details
     */
    @Transactional
    void subtract(List<? extends BaseOrderDetailDto> details);

    boolean existsByWarehouseId(Long warehouseId);
}
