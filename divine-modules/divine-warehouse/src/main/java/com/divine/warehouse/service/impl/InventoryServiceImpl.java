package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.entity.Inventory;
import com.divine.warehouse.domain.vo.BoardListVO;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.StorageShelfVO;
import com.divine.warehouse.mapper.InventoryMapper;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库存Service业务层处理
 *
 * @author yisl
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    private final InventoryMapper inventoryMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询库存
     */
    @Override
    public InventoryVo queryById(Long id) {
        return inventoryMapper.selectVoById(id);
    }

    /**
     * 查询库存列表
     */
    @Override
    public List<InventoryVo> queryList(InventoryDto dto) {
        LambdaQueryWrapper<Inventory> lqw = buildQueryWrapper(dto);
        List<InventoryVo> vos = inventoryMapper.selectVoList(lqw);
        if (CollUtil.isNotEmpty(vos)) {
            Set<Long> skuIds = vos.stream().map(InventoryVo::getSkuId).collect(Collectors.toSet());
            Map<Long, ItemSkuMapVo> itemSkuMap = itemSkuService.queryItemSkuMapVosByIds(skuIds);
            vos.forEach(it -> {
                ItemSkuMapVo itemSkuMapVo = itemSkuMap.get(it.getSkuId());
                it.setItemSku(itemSkuMapVo.getItemSku());
                it.setItem(itemSkuMapVo.getItem());
            });
        }
        return vos;
    }

    private LambdaQueryWrapper<Inventory> buildQueryWrapper(InventoryDto dto) {
        LambdaQueryWrapper<Inventory> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(dto.getSkuId() != null, Inventory::getSkuId, dto.getSkuId());
        wrapper.eq(dto.getWarehouseId() != null, Inventory::getWarehouseId, dto.getWarehouseId());
        wrapper.eq(dto.getQuantity() != null, Inventory::getQuantity, dto.getQuantity());
        return wrapper;
    }

    /**
     * 新增库存
     */
    @Override
    public void insertByBo(InventoryDto dto) {
        Inventory add = MapstructUtils.convert(dto, Inventory.class);
        inventoryMapper.insert(add);
    }

    /**
     * 修改库存
     */
    @Override
    public void updateByBo(InventoryDto dto) {
        Inventory update = MapstructUtils.convert(dto, Inventory.class);
        inventoryMapper.updateById(update);
    }

    /**
     * 批量删除库存
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        inventoryMapper.deleteBatchIds(ids);
    }

    /**
     * 校验规格是否有库存
     *
     * @param skuIds
     * @return
     */
    @Override
    public boolean existsBySkuIds(@NotEmpty Collection<Long> skuIds) {
        LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
        lqw.in(Inventory::getSkuId, skuIds);
        return inventoryMapper.exists(lqw);
    }

    /**
     * 查询库存列表仓库维度
     *
     * @param dto
     * @param basePage
     * @return
     */
    @Override
    public PageInfoRes<BoardListVO> queryWarehouseBoardList(InventoryDto dto, BasePage basePage) {
        Page<BoardListVO> result = inventoryMapper.queryWarehouseBoardList(basePage.build(), dto);
        // 2. 如果结果为空，直接返回
        if (result.getRecords() == null || result.getRecords().isEmpty()) {
            return PageInfoRes.build(result);
        }
        // 3. 解析 stock_info_text 为 List<StorageShelfVO>
        result.getRecords().forEach(this::parseStockInfo);
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存列表物品维度
     *
     * @param dto
     * @param basePage
     * @return
     */
    @Override
    public PageInfoRes<BoardListVO> queryItemBoardList(InventoryDto dto, BasePage basePage) {
        // 1. 执行主查询（现在返回的是带 stock_info_text 的 VO）
        Page<BoardListVO> result = inventoryMapper.queryItemBoardList(basePage.build(), dto);
        // 2. 如果结果为空，直接返回
        if (result.getRecords() == null || result.getRecords().isEmpty()) {
            return PageInfoRes.build(result);
        }
        // 3. 解析 stock_info_text 为 List<StorageShelfVO>
        result.getRecords().forEach(this::parseStockInfo);
        return PageInfoRes.build(result);
    }

    /**
     * 解析库存信息字符串为对象列表
     * 格式： "A-01:100;B-02:200;C-03:50"
     */
    private void parseStockInfo(BoardListVO vo) {
        String stockInfoText = vo.getStockInfoText();
        if (StringUtils.isBlank(stockInfoText)) {
            vo.setStockInfo(new ArrayList<>());
            return;
        }
        // todo 如果需要排除库存为0的货架
//        List<StorageShelfVO> stockLis = new ArrayList<>();
//        List<String> list = Arrays.stream(stockInfoText.split(";")).toList();
//        for (String s : list) {
//            String[] parts = s.split(":");
//            if ("0".equals(parts[1])) {
//                continue;
//            }
//            StorageShelfVO ss = new StorageShelfVO();
//            ss.setStorageShelf(parts[0]);
//            ss.setQuantity(Long.parseLong(parts[1]));
//            stockLis.add(ss);
//        }


        List<StorageShelfVO> stockList = Arrays.stream(stockInfoText.split(";"))
            .map(item -> {
                String[] parts = item.split(":");
                if (parts.length == 3) {
                    StorageShelfVO ss = new StorageShelfVO();
                    ss.setId(Long.parseLong(parts[0]));
                    ss.setStorageShelf(parts[1]);
                    ss.setQuantity(Long.parseLong(parts[2]));
                    return ss;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        vo.setStockInfo(stockList);
    }


    @Override
    public void updateInventory(List<CheckOrderDetailDto> details) {
        List<Inventory> updateInventoryList = new LinkedList<>();
        List<Inventory> insertInventoryList = new LinkedList<>();

        details.forEach(detail -> {
            LambdaQueryWrapper<Inventory> wrapper = Wrappers.lambdaQuery();
            if (detail.getInventoryId() != null) {
                wrapper.eq(Inventory::getId, detail.getInventoryId());
                Inventory inventory = inventoryMapper.selectOne(wrapper);
                if (inventory.getQuantity().compareTo(detail.getQuantity()) != 0) {
                    ItemSkuMapVo itemSkuMapVo = itemSkuService.queryItemSkuMapVo(detail.getSkuId());
                    log.error("账面库存不匹配：" + itemSkuMapVo.getItem().getItemName() + "（" + itemSkuMapVo.getItemSku().getSkuName() + "）,填写账面库存：" + detail.getQuantity() + " 实际账面库存：" + inventory.getQuantity());
                    throw new BusinessException("账面库存不匹配");
                } else {
                    if (!inventory.getQuantity().equals(detail.getCheckQuantity())) {
                        inventory.setQuantity(detail.getCheckQuantity());
                        updateInventoryList.add(inventory);
                    }
                }
            } else {
                wrapper.eq(Inventory::getSkuId, detail.getSkuId());
                wrapper.eq(Inventory::getWarehouseId, detail.getWarehouseId());
                Inventory inventory = inventoryMapper.selectOne(wrapper);
                if (inventory != null) {
                    ItemSkuMapVo itemSkuMapVo = itemSkuService.queryItemSkuMapVo(detail.getSkuId());
                    log.error("账面库存不匹配：" + itemSkuMapVo.getItem().getItemName() + "（" + itemSkuMapVo.getItemSku().getSkuName() + "），填写账面库存：0, 实际账面库存：" + inventory.getQuantity());
                    throw new BusinessException("账面库存不匹配");
                } else {
                    inventory = new Inventory();
                    inventory.setSkuId(detail.getSkuId());
                    inventory.setWarehouseId(detail.getWarehouseId());
                    inventory.setQuantity(detail.getCheckQuantity());
                    insertInventoryList.add(inventory);
                }
            }
        });
        if (CollUtil.isNotEmpty(updateInventoryList)) {
            inventoryMapper.updateBatchById(updateInventoryList);
        }
        if (CollUtil.isNotEmpty(insertInventoryList)) {
            inventoryMapper.insertBatch(insertInventoryList);
        }
    }

    @Override
    @Transactional
    public void add(List<? extends BaseOrderDetailDto> details) {
        List<Inventory> addList = new LinkedList<>();
        List<Inventory> updateList = new LinkedList<>();
        details.forEach(orderDetailsBo -> {
            LambdaQueryWrapper<Inventory> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Inventory::getWarehouseId, orderDetailsBo.getWarehouseId());
            wrapper.eq(Inventory::getSkuId, orderDetailsBo.getSkuId());
            wrapper.eq(Inventory::getStorageShelf, orderDetailsBo.getStorageShelf());
            Inventory result = inventoryMapper.selectOne(wrapper);
            if (result != null) {
                Long before = result.getQuantity();
                Long after = before + orderDetailsBo.getQuantity();
                result.setQuantity(after);
                orderDetailsBo.setAfterQuantity(after);
                orderDetailsBo.setBeforeQuantity(before);
                updateList.add(result);
            } else {
                orderDetailsBo.setBeforeQuantity(0L);
                orderDetailsBo.setAfterQuantity(orderDetailsBo.getQuantity());
                Inventory inventory = MapstructUtils.convert(orderDetailsBo, Inventory.class);
                addList.add(inventory);
            }
        });
        if (!addList.isEmpty()) {
            saveBatch(addList);
        }
        if (!updateList.isEmpty()) {
            updateBatchById(updateList);
        }
    }

    /**
     * 扣减库存
     *
     * @param details
     */
    @Override
    @Transactional
    public void subtract(List<? extends BaseOrderDetailDto> details) {
        List<Inventory> updateList = new LinkedList<>();
        details.forEach(shipmentOrderDetailBo -> {
            LambdaQueryWrapper<Inventory> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Inventory::getWarehouseId, shipmentOrderDetailBo.getWarehouseId());
            wrapper.eq(Inventory::getSkuId, shipmentOrderDetailBo.getSkuId());
            Inventory result = inventoryMapper.selectOne(wrapper);
            if (result == null) {
                ItemSkuMapVo itemSkuMapVo = itemSkuService.queryItemSkuMapVo(shipmentOrderDetailBo.getSkuId());
                log.error(itemSkuMapVo.getItem().getItemName() + "（" + itemSkuMapVo.getItemSku().getSkuName() + "）库存不足，当前库存：0");
                throw new BusinessException("库存不足");
            }
            Long beforeQuantity = result.getQuantity();
            Long afterQuantity = beforeQuantity - shipmentOrderDetailBo.getQuantity();
            if (afterQuantity < 0) {
                ItemSkuMapVo itemSkuMapVo = itemSkuService.queryItemSkuMapVo(shipmentOrderDetailBo.getSkuId());
                log.error(itemSkuMapVo.getItem().getItemName() + "（" + itemSkuMapVo.getItemSku().getSkuName() + "）库存不足，当前库存：" + beforeQuantity);
                throw new BusinessException("库存不足");
            }
            shipmentOrderDetailBo.setBeforeQuantity(beforeQuantity);
            shipmentOrderDetailBo.setAfterQuantity(afterQuantity);
            result.setQuantity(afterQuantity);
            updateList.add(result);
        });
        updateBatchById(updateList);
    }

    @Override
    public boolean existsByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
        lqw.eq(Inventory::getWarehouseId, warehouseId);
        return inventoryMapper.exists(lqw);
    }

    /**
     * 删除货架
     *
     * @param id
     */
    @Override
    public void deleteStorageShelf(Long id) {
        // 查询货架是否还有库存
        Inventory inventory = inventoryMapper.selectById(id);
        Long quantity = inventory.getQuantity();
        if (quantity > 0) {
            throw new BusinessException("该货架上还有库存，暂时不可删除");
        }
        // 库存为0 可以删除
        inventoryMapper.deleteById(id);
    }
}
