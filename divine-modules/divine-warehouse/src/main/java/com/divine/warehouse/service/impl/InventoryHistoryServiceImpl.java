package com.divine.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.BaseOrderDto;
import com.divine.warehouse.domain.dto.InventoryHistoryDto;
import com.divine.warehouse.domain.entity.InventoryHistory;
import com.divine.warehouse.domain.vo.InventoryHistoryVo;
import com.divine.warehouse.mapper.InventoryHistoryMapper;
import com.divine.warehouse.service.InventoryHistoryService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 库存记录Service业务层处理
 *
 * @author yisl
 * @date 2024-07-22
 */
@RequiredArgsConstructor
@Service
public class InventoryHistoryServiceImpl extends ServiceImpl<InventoryHistoryMapper, InventoryHistory> implements InventoryHistoryService {

    private final InventoryHistoryMapper inventoryHistoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInventoryHistory(BaseOrderDto<? extends BaseOrderDetailDto> dto, Integer orderType, Boolean isAdd) {
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        dto.getDetails().forEach(detail -> {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(dto.getId());
            inventoryHistory.setOrderNo(dto.getBizNo());
            inventoryHistory.setOrderType(orderType);
            inventoryHistory.setSkuId(detail.getSkuId());
            if (isAdd) {
                inventoryHistory.setQuantity(detail.getQuantity());
            } else {
                inventoryHistory.setQuantity(-detail.getQuantity());
            }
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setUnitPrice(detail.getUnitPrice());
            inventoryHistory.setBeforeQuantity(detail.getBeforeQuantity());
            inventoryHistory.setAfterQuantity(detail.getAfterQuantity());
            inventoryHistoryList.add(inventoryHistory);
        });
        this.saveBatch(inventoryHistoryList);
    }

    /**
     * 查询库存记录
     */
    @Override
    public InventoryHistoryVo queryById(Long id) {
        return inventoryHistoryMapper.selectVoById(id);
    }

    /**
     * 查询库存记录列表
     */
    @Override
    public PageInfoRes<InventoryHistoryVo> queryPageList(InventoryHistoryDto dto, BasePage basePage) {
        Page<InventoryHistoryVo> result = inventoryHistoryMapper.selectVoPageByBo(basePage.build(), dto);
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存记录列表
     */
    @Override
    public List<InventoryHistoryVo> queryList(InventoryHistoryDto dto) {
        LambdaQueryWrapper<InventoryHistory> lqw = buildQueryWrapper(dto);
        return inventoryHistoryMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InventoryHistory> buildQueryWrapper(InventoryHistoryDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<InventoryHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getOrderId() != null, InventoryHistory::getOrderId, dto.getOrderId());
        lqw.eq(dto.getOrderType() != null, InventoryHistory::getOrderType, dto.getOrderType());
        lqw.eq(dto.getSkuId() != null, InventoryHistory::getSkuId, dto.getSkuId());
        lqw.eq(dto.getQuantity() != null, InventoryHistory::getQuantity, dto.getQuantity());
        lqw.eq(dto.getWarehouseId() != null, InventoryHistory::getWarehouseId, dto.getWarehouseId());
        return lqw;
    }

    /**
     * 新增库存记录
     */
    @Override
    public void insertByBo(InventoryHistoryDto dto) {
        InventoryHistory add = MapstructUtils.convert(dto, InventoryHistory.class);
        inventoryHistoryMapper.insert(add);
    }

    /**
     * 修改库存记录
     */
    @Override
    public void updateByBo(InventoryHistoryDto dto) {
        InventoryHistory update = MapstructUtils.convert(dto, InventoryHistory.class);
        inventoryHistoryMapper.updateById(update);
    }

    /**
     * 批量删除库存记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        inventoryHistoryMapper.deleteBatchIds(ids);
    }
}
