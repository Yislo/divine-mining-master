package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.constant.NoticeConstant;
import com.divine.common.core.constant.RedisKeyConstants;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.redis.utils.RedisDistributedLock;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.system.domain.dto.SysNoticeDto;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.system.service.CommonService;
import com.divine.system.service.SysNoticeService;
import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.dto.InventoryDto;
import com.divine.warehouse.domain.entity.Inventory;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.*;
import com.divine.warehouse.mapper.InventoryMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.ItemService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final ItemService itemService;
    private final RedisDistributedLock redisDistributedLock;
    private final CommonService commonService;
    private final SysNoticeService noticeService;
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询库存
     */
    @Override
    public InventoryVo queryById(Long id) {
        InventoryVo inventoryVo = inventoryMapper.selectVoById(id);
        // 查询sku信息
        Long skuId = inventoryVo.getSkuId();
        ItemSkuVo itemSkuVo = itemSkuService.queryById(skuId);
        inventoryVo.setItemSku(itemSkuVo);
        // 查询item信息
        ItemVo item = itemService.queryById(itemSkuVo.getItemId());
        inventoryVo.setItem(item);
        return inventoryVo;
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
                String storageShelf = it.getStorageShelf();
                it.setStorageShelf(storageShelf);
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
    public void deleteById(Long id) {
        // 查询货架是否还有库存
        Inventory inventory = inventoryMapper.selectById(id);
        Long quantity = inventory.getQuantity();
        if (quantity > 0) {
            throw new BusinessException("该货架上还有库存，暂时不可删除");
        }
        inventoryMapper.deleteById(id);
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
        return PageInfoRes.build(result);
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
        // 获取预警参数
        SysConfigVo configParam = commonService.getConfigParam("sys.warehouse.warning");

        for (BaseOrderDetailDto d : details) {
            String lockKey = d.getWarehouseId() + d.getSkuId() + d.getStorageShelf();
            String requestId = redisDistributedLock.lock("stock");
            try {
                Inventory inv = inventoryMapper.selectOne(
                    Wrappers.lambdaQuery(Inventory.class)
                        .eq(Inventory::getWarehouseId, d.getWarehouseId())
                        .eq(Inventory::getSkuId, d.getSkuId())
                        .eq(Inventory::getStorageShelf, d.getStorageShelf())
                );
                if (inv == null) {
                    throw new BusinessException("未找到相关物品信息");
                }
                // 扣减前数量
                Long beforeQuantity = inv.getQuantity();
                int rows = inventoryMapper.subtractStock(
                    d.getWarehouseId(),
                    d.getSkuId(),
                    d.getStorageShelf(),
                    d.getQuantity()
                );
                if (rows == 0) {
                    ItemSkuVo itemSkuVo = itemSkuService.queryById(inv.getSkuId());
                    throw new BusinessException(itemSkuVo.getSkuNo() + "库存不足");
                }
                d.setBeforeQuantity(beforeQuantity);
                // 扣减后数量
                long afterQuantity = beforeQuantity - d.getQuantity();
                d.setAfterQuantity(afterQuantity);
                // 库存是否到达预警值
                // 如果到达则发送预警消息通知-每日只发送一次
                long safeStock = Long.parseLong(configParam.getConfigValue());
                if (afterQuantity <= safeStock) {
                    sendNotice(inv.getWarehouseId(), d.getSkuId(), safeStock, afterQuantity, d.getStorageShelf());
                    //存入redis,确保每天每个物品只发送一次
                    RedisUtils.set(RedisKeyConstants.STOCK_WARING_NOTICE_KEY + d.getSkuId(), d.getSkuId(), RedisUtils.getSecondsUntilMidnight());
                }

            } finally {
                redisDistributedLock.unlock(lockKey, requestId);
            }
        }
    }

    /**
     * 发送库存预警通知
     *
     * @param skuId
     */
    private void sendNotice(Long warehouseId,
                            Long skuId,
                            Long safeStock,
                            Long afterQuantity,
                            String storageShelf) {
        SysNoticeDto sysNotice = new SysNoticeDto();
        sysNotice.setNoticeType(1);
        sysNotice.setEventType(1);
        sysNotice.setNoticeTitle(NoticeConstant.STOCK_WARNING_TITLE);
        sysNotice.setNoticeContent(generateContent(warehouseId, skuId, safeStock, afterQuantity, storageShelf));
        noticeService.sendMessage(sysNotice);
    }

    /**
     * 生成预警消息内容（标准版）
     */
    private String generateContent(Long warehouseId,
                                   Long skuId,
                                   Long safeStock,
                                   Long afterQuantity,
                                   String storageShelf) {
        ItemSkuVo sku = itemSkuService.queryById(skuId);
        ItemVo item = itemService.queryById(sku.getItemId());
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format(
            "⚠️ 【库存预警通知】\n\n" +
                "尊敬的负责人：\n\n" +
                "您好！系统检测到以下商品库存已低于安全库存线，请及时安排补货：\n\n" +
                "📦 物品名称：%s\n" +
                "🏷️ SKU编码：%s\n" +
                "📍 所在仓库：%s\n" +
                "📊 当前库存：%d %s\n" +
                "⚠️ 安全库存：%d %s\n" +
                "⏰ 预警时间：%s\n\n" +
                "请尽快处理！",

            item.getItemName() + "(" + sku.getSkuName() + ")",                // 商品名称
            sku.getSkuNo(),                                             // SKU编码
            warehouse.getWarehouseName() + "-" + storageShelf + "货架",      // 仓库名称
            afterQuantity,                                              // 当前库存
            item.getUnit(),                                             // 单位
            safeStock,                                                  // 安全库存
            item.getUnit(),                                             // 单位
            currentTime                                                 // 预警时间
        );
    }


    @Override
    public boolean existsByWarehouseId(Long warehouseId) {
        LambdaQueryWrapper<Inventory> lqw = Wrappers.lambdaQuery();
        lqw.eq(Inventory::getWarehouseId, warehouseId);
        return inventoryMapper.exists(lqw);
    }

    /**
     * 根据skuIds查询
     *
     * @param skuIds
     * @return
     */
    @Override
    public List<InventoryVo> getBySkuIds(List<Long> skuIds) {
        if (CollectionUtil.isEmpty(skuIds)) {
            return List.of();
        }
        return inventoryMapper.selectVoList(new LambdaQueryWrapper<>(Inventory.class)
            .in(Inventory::getSkuId, skuIds));
    }

    /**
     * 获取指定货架库存
     * @param skuId
     * @param storageShelf
     * @return
     */
    @Override
    public Long getStock(Long skuId, String storageShelf) {
        Inventory inventory = inventoryMapper.selectOne(new LambdaQueryWrapper<>(Inventory.class)
            .eq(Inventory::getSkuId, skuId)
            .eq(Inventory::getStorageShelf, storageShelf));
        if (ObjUtil.isNull(inventory)){
            return 0L;
        }
        return inventory.getQuantity();
    }
}
