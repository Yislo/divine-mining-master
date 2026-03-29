package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.entity.ItemSku;
import com.divine.warehouse.domain.vo.BaseOrderDetailVO;
import com.divine.warehouse.domain.vo.InventoryVo;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.warehouse.mapper.ItemSkuMapper;
import com.divine.system.service.CommonService;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.google.api.client.util.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
@Slf4j
public class ItemSkuServiceImpl extends ServiceImpl<ItemSkuMapper, ItemSku> implements ItemSkuService {


    private final ItemSkuMapper itemSkuMapper;
    private final InventoryService inventoryService;
    private final GenerateNoUtil generateNoUtil;

    /**
     * 查询sku信息
     */
    @Override
    public ItemSkuMapVo queryItemSkuMapVo(Long id) {
        return itemSkuMapper.queryItemSkuMapVo(id);
    }

    @Override
    public ItemSkuVo queryById(Long id) {
        return itemSkuMapper.selectVoById(id);
    }


    /**
     * 查询sku信息列表，用于出入库的选择组件
     */
    @Override
    public PageInfoRes<ItemSkuMapVo> queryPageList(ItemSkuDto dto, BasePage basePage) {
        //开始查sku
        IPage<ItemSkuMapVo> result = itemSkuMapper.selectByBo(basePage.build(), dto);
        return PageInfoRes.build(result);
    }

    /**
     * 查询sku信息列表
     */
    @Override
    public List<ItemSkuVo> queryList(ItemSkuDto dto) {
        LambdaQueryWrapper<ItemSku> lqw = buildQueryWrapper(dto);
        return itemSkuMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ItemSku> buildQueryWrapper(ItemSkuDto dto) {
        LambdaQueryWrapper<ItemSku> lqw = Wrappers.lambdaQuery();
//        lqw.and(w -> w.like(StrUtil.isNotBlank(dto.getSkuName()), ItemSku::getSkuName, dto.getSkuName())
//            .or()
//            .like(StrUtil.isNotBlank(dto.getSkuName()), ItemSku::getSkuNo, dto.getSkuName()));
        lqw.like(StrUtil.isNotBlank(dto.getSkuName()), ItemSku::getSkuName, dto.getSkuName());
        lqw.in(CollUtil.isNotEmpty(dto.getItemIdList()), ItemSku::getItemId, dto.getItemIdList());
        lqw.eq(dto.getItemId() != null, ItemSku::getItemId, dto.getItemId());
        lqw.orderByDesc(ItemSku::getItemId);
        return lqw;
    }

    /**
     * 新增sku信息
     */
    @Override
    public Boolean insertByBo(ItemSkuDto dto) {
        ItemSku add = MapstructUtils.convert(dto, ItemSku.class);
        add.setSkuNo(generateNoUtil.getBizNo(NoTypeEnum.SKU_NO.getCode()));
        return itemSkuMapper.insert(add) > 0;
    }

    /**
     * 修改sku信息
     */
    @Override
    public Boolean updateByBo(ItemSkuDto dto) {
        ItemSku update = MapstructUtils.convert(dto, ItemSku.class);
        return itemSkuMapper.updateById(update) > 0;
    }

    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        itemSkuMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        // 只有一个不能删除
        ItemSku itemSku = itemSkuMapper.selectById(id);

        if (queryByItemId(itemSku.getItemId()).size() <= 1) {
            throw new BusinessException("至少包含一个物品规格！");
        }
        // 校验库存是否已关联
        if (inventoryService.existsBySkuIds(List.of(id))) {
            throw new BusinessException("该规格已有业务关联，无法删除！");
        }
    }

    private void validateSkuIdsBeforeDelete(Collection<Long> skuIds) {
        if (inventoryService.existsBySkuIds(skuIds)) {
            throw new BusinessException("该物品已有业务关联，无法删除！");
        }
    }

    /**
     * 批量删除sku信息
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        // 校验库存是否已关联
        validateSkuIdsBeforeDelete(ids);
        // 删除
        itemSkuMapper.deleteBatchIds(ids);
    }

    /**
     * 批量保存物品sku
     *
     * @param sku 物品sku
     */
    @Override
    @Transactional
    public void saveOrUpdateBatchByBo(List<ItemSkuDto> sku) {
        List<ItemSku> itemSkuList = MapstructUtils.convert(sku, ItemSku.class);
        itemSkuList.forEach(s -> s.setSkuNo(generateNoUtil.getBizNo(NoTypeEnum.SKU_NO.getCode())));
        saveOrUpdateBatch(itemSkuList);
    }

    public void setItemId(List<ItemSkuDto> itemSkuList, Long itemId) {
        for (ItemSkuDto itemSkuDto : itemSkuList) {
//            if (StrUtil.isBlank(itemSkuDto.getBarcode())) {
            itemSkuDto.setItemId(itemId);
//            }
        }
    }

    /**
     * 查询sku列表
     *
     * @param id 物品id
     */
    @Override
    public List<ItemSkuVo> queryByItemId(Long id) {
        LambdaQueryWrapper<ItemSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemSku::getItemId, id);
        return itemSkuMapper.selectVoList(lqw);
    }

    @Override
    public Map<Long, ItemSkuMapVo> queryItemSkuMapVosByIds(Set<Long> skuIds) {
        return itemSkuMapper.queryItemSkuMapVos(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuMapVo::getSkuId, Function.identity()));
    }

    @Override
    public void setItemSkuMap(List<? extends BaseOrderDetailVO> details) {
        if (CollUtil.isNotEmpty(details)) {
            Set<Long> skuIds = details
                .stream()
                .map(BaseOrderDetailVO::getSkuId)
                .collect(Collectors.toSet());

            Map<Long, ItemSkuMapVo> itemSkuMap = this.queryItemSkuMapVosByIds(skuIds);

            details.forEach(detail -> {
                ItemSkuMapVo vo = itemSkuMap.get(detail.getSkuId());
                if (ObjUtil.isNotNull(vo)) {
                    detail.setItemSku(vo.getItemSku());
                    detail.setItem(vo.getItem());
                }
            });
        }
    }

    /**
     * 获取sku货架信息
     *
     * @param skuId
     * @return
     */
    @Override
    public List<String> getStorage(Long skuId) {
        List<InventoryVo> bySkuIds = inventoryService.getBySkuIds(Collections.singletonList(skuId));
        if (CollUtil.isEmpty(bySkuIds)) {
            return Collections.emptyList();
        }
        return bySkuIds.stream().map(InventoryVo::getStorageShelf).toList();
    }
}
