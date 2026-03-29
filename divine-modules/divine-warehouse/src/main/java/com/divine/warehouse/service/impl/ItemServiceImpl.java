package com.divine.warehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.warehouse.domain.dto.ItemDto;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.entity.Item;
import com.divine.warehouse.domain.entity.ItemCategory;
import com.divine.warehouse.domain.vo.ItemCategoryVo;
import com.divine.warehouse.domain.vo.ItemInfoVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.warehouse.domain.vo.ItemVo;
import com.divine.warehouse.mapper.ItemCategoryMapper;
import com.divine.warehouse.mapper.ItemMapper;
import com.divine.warehouse.service.ItemService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemSkuService itemSkuService;
    private final ItemCategoryMapper itemCategoryMapper;
    private final GenerateNoUtil generateNoUtil;

    /**
     * 查询物料
     */
    @Override
    public ItemVo queryById(Long id) {
        return itemMapper.selectVoById(id);
    }

    /**
     * 查询物料
     *
     * @param itemIds ids
     */
    @Override
    public List<ItemVo> queryById(List<Long> itemIds) {
        if (CollUtil.isEmpty(itemIds)) {
            return CollUtil.newArrayList();
        }
        LambdaQueryWrapper<Item> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.in(Item::getId, itemIds);
        return itemMapper.selectVoList(lambdaQueryWrapper);
    }

    /**
     * 查询物料列表
     */
    @Override
    public PageInfoRes<ItemVo> queryPageList(ItemDto dto, BasePage basePage) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(dto);
        Page<ItemVo> result = itemMapper.selectVoPage(basePage.build(), lqw);
        List<ItemVo> itemVoList = result.getRecords();
        if (!CollUtil.isEmpty(itemVoList)) {
            LambdaQueryWrapper<ItemCategory> itemTypeWrapper = new LambdaQueryWrapper<>();
            itemTypeWrapper.in(ItemCategory::getId, itemVoList.stream().map(ItemVo::getItemCategory).collect(Collectors.toSet()));
            Map<Long, ItemCategoryVo> itemCategoryVoMap = itemCategoryMapper.selectVoList(itemTypeWrapper).stream().collect(Collectors.toMap(ItemCategoryVo::getId, Function.identity()));
            itemVoList.forEach(itemVo -> itemVo.setItemCategoryInfo(itemCategoryVoMap.get(Long.valueOf(itemVo.getItemCategory()))));
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询物料列表
     */
    @Override
    public List<ItemVo> queryList(ItemDto dto) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(dto);
        return itemMapper.selectVoList(lqw);
    }

    /**
     * 查询物品详情
     *
     * @param dto
     * @return
     */
    @Override
    public List<ItemInfoVo> queryItemInfo(ItemDto dto) {
        // 1. 参数校验：如果没有任何查询条件，直接返回空，避免全表扫描
        if (StringUtils.isAllBlank(dto.getItemNo(), dto.getItemName(), dto.getSkuNoOrName())) {
            return Collections.emptyList();
        }
        // 2. 查询 Item 基础信息
        List<ItemVo> itemVos = queryList(dto);
        if (CollUtil.isEmpty(itemVos)) {
            return Collections.emptyList();
        }
        // 3. 转换 VO 并提取 ID 列表
        List<ItemInfoVo> itemInfoVos = BeanUtil.copyToList(itemVos, ItemInfoVo.class);
        List<Long> itemIdList = itemInfoVos.stream()
            .map(ItemInfoVo::getId)
            .filter(Objects::nonNull)
            .toList();
        // 4. 批量查询 SKU 信息 (In查询性能更佳)
        Map<Long, List<ItemSkuVo>> skuMap = Collections.emptyMap();
        if (CollUtil.isNotEmpty(itemIdList)) {
            ItemSkuDto itemSkuDto = new ItemSkuDto();
            itemSkuDto.setItemIdList(itemIdList);
            itemSkuDto.setSkuName(dto.getSkuNoOrName());
            List<ItemSkuVo> itemSkuVos = itemSkuService.queryList(itemSkuDto);
            // 分组处理
            skuMap = itemSkuVos.stream()
                .collect(Collectors.groupingBy(ItemSkuVo::getItemId));
        }

        // 5. 聚合数据
        for (ItemInfoVo item : itemInfoVos) {
            item.setSkuList(skuMap.getOrDefault(item.getId(), Collections.emptyList()));
        }

        return itemInfoVos;
    }

    private LambdaQueryWrapper<Item> buildQueryWrapper(ItemDto dto) {
        LambdaQueryWrapper<Item> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(dto.getItemNo()), Item::getItemNo, dto.getItemNo());
        lqw.and(StrUtil.isNotBlank(dto.getItemName()),w ->
            w.eq(StrUtil.isNotBlank(dto.getItemName()), Item::getItemNo, dto.getItemName())
                .or()
                .like(StrUtil.isNotBlank(dto.getItemName()), Item::getItemName, dto.getItemName()));
        // 主键集合
        lqw.in(!CollUtil.isEmpty(dto.getIds()), Item::getId, dto.getIds());
        if (!StrUtil.isBlank(dto.getItemCategory())) {
            Long parentId = Long.valueOf(dto.getItemCategory());
            List<Long> subIdList = this.buildSubItemCategoryIdList(parentId);
            subIdList.add(Long.valueOf(dto.getItemCategory()));
            lqw.in(Item::getItemCategory, subIdList);
        }
        lqw.eq(StrUtil.isNotBlank(dto.getUnit()), Item::getUnit, dto.getUnit());
        return lqw;
    }

    private List<Long> buildSubItemCategoryIdList(Long parentId) {
        LambdaQueryWrapper<ItemCategory> itemTypeWrapper = new LambdaQueryWrapper<>();
        itemTypeWrapper.eq(ItemCategory::getParentId, parentId);
        return itemCategoryMapper.selectList(itemTypeWrapper).stream().map(ItemCategory::getId).collect(Collectors.toList());
    }

    /**
     * 新增物料
     *
     * @param dto
     */
    @Override
    @Transactional
    public void insertByForm(ItemDto dto) {
        validateBoBeforeSave(dto);
        Item item = MapstructUtils.convert(dto, Item.class);
        item.setItemNo(generateNoUtil.getBizNo(NoTypeEnum.SPU_NO.getCode()));
        itemMapper.insert(item);
        itemSkuService.setItemId(dto.getSku(), item.getId());
        itemSkuService.saveOrUpdateBatchByBo(dto.getSku());
    }

    /**
     * 修改物料
     *
     * @param dto
     */
    @Override
    @Transactional
    public void updateByForm(ItemDto dto) {
        validateBoBeforeSave(dto);
        itemMapper.updateById(MapstructUtils.convert(dto, Item.class));
        itemSkuService.setItemId(dto.getSku(), dto.getId());
        itemSkuService.saveOrUpdateBatchByBo(dto.getSku());
    }

    /**
     * 保存前的数据校验
     */
    private void validateBoBeforeSave(ItemDto itemDto) {
        validateItemName(itemDto);
        validateItemSkuName(itemDto.getSku());
    }

    private void validateItemName(ItemDto item) {
        LambdaQueryWrapper<Item> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Item::getItemName, item.getItemName());
        queryWrapper.ne(item.getId() != null, Item::getId, item.getId());
        if (itemMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException("物品名称重复");
        }
    }

    private void validateItemSkuName(List<ItemSkuDto> skuVoList) {
        if (skuVoList.stream().map(ItemSkuDto::getSkuName).distinct().count() != skuVoList.size()) {
            throw new BusinessException("物品规格重复");
        }
    }


    /**
     * 批量删除物料
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        List<Long> skuIds = itemSkuService.queryByItemId(id).stream().map(ItemSkuVo::getId).toList();
        itemMapper.deleteById(id);
        itemSkuService.deleteByIds(skuIds);
    }

}
