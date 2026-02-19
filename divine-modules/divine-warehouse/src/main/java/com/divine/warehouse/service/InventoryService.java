package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.vo.BoardListVO;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 库存Service业务层处理
 *
 * @author yisl
 * @date 2024-07-19
 */
public interface InventoryService {

    /**
     * 查询库存
     * @param id
     * @return
     */
    InventoryVo queryById(Long id);

    /**
     * 查询库存列表
     * @param dto
     * @return
     */
    List<InventoryVo> queryList(InventoryDto dto);

    /**
     * 新增库存
     * @param dto
     */
    void insertByBo(InventoryDto dto);

    /**
     * 修改库存
     * @param dto
     */
    void updateByBo(InventoryDto dto);

    /**
     * 批量删除库存
     * @param ids
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 校验规格是否有库存
     *
     * @param skuIds
     * @return
     */
    boolean existsBySkuIds(@NotEmpty Collection<Long> skuIds);

    /**
     * 查询库存列表-仓库维度
     *
     * @param dto
     * @param basePage
     * @return
     */
    PageInfoRes<BoardListVO> queryWarehouseBoardList(InventoryDto dto, BasePage basePage);

    /**
     * 查询库存列表-物品维度
     *
     * @param dto
     * @param basePage
     * @return
     */
    PageInfoRes<BoardListVO> queryItemBoardList(InventoryDto dto, BasePage basePage);

    /**
     * 更新库存
     * @param details
     */
    void updateInventory(List<CheckOrderDetailDto> details);

    /**
     * 新增库存
     * @param details
     */
    @Transactional
    void add(List<? extends BaseOrderDetailDto> details);

    /**
     * 扣减库存
     *
     * @param details
     */
    @Transactional
    void subtract(List<? extends BaseOrderDetailDto> details);

    /**
     * 判断仓库是否有绑定业务
     * @param warehouseId
     * @return
     */
    boolean existsByWarehouseId(Long warehouseId);

    /**
     * 删除货架
     * @param id
     */
    void deleteStorageShelf(Long id);
}
