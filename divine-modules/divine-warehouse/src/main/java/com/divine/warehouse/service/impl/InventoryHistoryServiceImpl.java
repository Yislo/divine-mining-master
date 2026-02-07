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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 库存记录Service业务层处理
 *
 * @author zcc
 * @date 2024-07-22
 */
@RequiredArgsConstructor
@Service
public class InventoryHistoryServiceImpl extends ServiceImpl<InventoryHistoryMapper, InventoryHistory> implements InventoryHistoryService {

    private final InventoryHistoryMapper inventoryHistoryMapper;

    @Override
    public void saveInventoryHistory(BaseOrderDto<? extends BaseOrderDetailDto> bo, Integer orderType, Boolean isAdd){
        List<InventoryHistory> inventoryHistoryList = new LinkedList<>();
        bo.getDetails().forEach(detail -> {
            InventoryHistory inventoryHistory = new InventoryHistory();
            inventoryHistory.setOrderId(bo.getId());
            inventoryHistory.setOrderNo(bo.getOrderNo());
            inventoryHistory.setOrderType(orderType);
            inventoryHistory.setSkuId(detail.getSkuId());
            if(isAdd){
                inventoryHistory.setQuantity(detail.getQuantity());
            }else {
                inventoryHistory.setQuantity(detail.getQuantity().negate());
            }
            inventoryHistory.setWarehouseId(detail.getWarehouseId());
            inventoryHistory.setAmount(detail.getAmount());
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
    public InventoryHistoryVo queryById(Long id){
        return inventoryHistoryMapper.selectVoById(id);
    }

    /**
     * 查询库存记录列表
     */
    @Override
    public PageInfoRes<InventoryHistoryVo> queryPageList(InventoryHistoryDto bo, BasePage basePage) {
        Page<InventoryHistoryVo> result = inventoryHistoryMapper.selectVoPageByBo(basePage.build(), bo);
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存记录列表
     */
    @Override
    public List<InventoryHistoryVo> queryList(InventoryHistoryDto bo) {
        LambdaQueryWrapper<InventoryHistory> lqw = buildQueryWrapper(bo);
        return inventoryHistoryMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InventoryHistory> buildQueryWrapper(InventoryHistoryDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<InventoryHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderId() != null, InventoryHistory::getOrderId, bo.getOrderId());
        lqw.eq(bo.getOrderType() != null, InventoryHistory::getOrderType, bo.getOrderType());
        lqw.eq(bo.getSkuId() != null, InventoryHistory::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, InventoryHistory::getQuantity, bo.getQuantity());
        lqw.eq(bo.getWarehouseId() != null, InventoryHistory::getWarehouseId, bo.getWarehouseId());
        return lqw;
    }

    /**
     * 新增库存记录
     */
    @Override
    public void insertByBo(InventoryHistoryDto bo) {
        InventoryHistory add = MapstructUtils.convert(bo, InventoryHistory.class);
        inventoryHistoryMapper.insert(add);
    }

    /**
     * 修改库存记录
     */
    @Override
    public void updateByBo(InventoryHistoryDto bo) {
        InventoryHistory update = MapstructUtils.convert(bo, InventoryHistory.class);
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
