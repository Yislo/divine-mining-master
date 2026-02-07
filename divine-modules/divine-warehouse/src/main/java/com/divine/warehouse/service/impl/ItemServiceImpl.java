package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.ItemDto;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.entity.Item;
import com.divine.warehouse.domain.entity.ItemCategory;
import com.divine.warehouse.domain.vo.ItemCategoryVo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemSkuService itemSkuService;
    private final ItemCategoryMapper itemCategoryMapper;

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
    public PageInfoRes<ItemVo> queryPageList(ItemDto bo, BasePage basePage) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        Page<ItemVo> result = itemMapper.selectVoPage(basePage.build(), lqw);
        List<ItemVo> itemVoList = result.getRecords();
        if (!CollUtil.isEmpty(itemVoList)) {
            LambdaQueryWrapper<ItemCategory> itemTypeWrapper = new LambdaQueryWrapper<>();
            itemTypeWrapper.in(ItemCategory::getId, itemVoList.stream().map(ItemVo::getItemCategory).collect(Collectors.toSet()));
            Map<Long, ItemCategoryVo> itemCategoryVoMap = itemCategoryMapper.selectVoList(itemTypeWrapper).stream().collect(Collectors.toMap(ItemCategoryVo::getId, Function.identity()));
            itemVoList.forEach(itemVo -> {
                itemVo.setItemCategoryInfo(itemCategoryVoMap.get(Long.valueOf(itemVo.getItemCategory())));
            });
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询物料列表
     */
    @Override
    public List<ItemVo> queryList(ItemDto bo) {
        LambdaQueryWrapper<Item> lqw = buildQueryWrapper(bo);
        return itemMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Item> buildQueryWrapper(ItemDto bo) {
        LambdaQueryWrapper<Item> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getItemCode()), Item::getItemCode, bo.getItemCode());
        // 主键集合
        lqw.in(!CollUtil.isEmpty(bo.getIds()), Item::getId, bo.getIds());
        lqw.like(StrUtil.isNotBlank(bo.getItemName()), Item::getItemName, bo.getItemName());
        if (!StrUtil.isBlank(bo.getItemCategory())){
            Long parentId = Long.valueOf(bo.getItemCategory());
            List<Long> subIdList = this.buildSubItemCategoryIdList(parentId);
            subIdList.add(Long.valueOf(bo.getItemCategory()));
            lqw.in(Item::getItemCategory, subIdList);
        }
        lqw.eq(StrUtil.isNotBlank(bo.getUnit()), Item::getUnit, bo.getUnit());
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
     * @param bo
     */
    @Override
    @Transactional
    public void insertByForm(ItemDto bo) {
        validateBoBeforeSave(bo);
        Item item = MapstructUtils.convert(bo, Item.class);
        itemMapper.insert(item);
        itemSkuService.setItemId(bo.getSku(),item.getId());
        itemSkuService.saveOrUpdateBatchByBo(bo.getSku());
    }

    /**
     * 修改物料
     *
     * @param bo
     */
    @Override
    @Transactional
    public void updateByForm(ItemDto bo) {
        validateBoBeforeSave(bo);
        itemMapper.updateById(MapstructUtils.convert(bo, Item.class));
        itemSkuService.setItemId(bo.getSku(),bo.getId());
        itemSkuService.saveOrUpdateBatchByBo(bo.getSku());
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
        Assert.isTrue(itemMapper.selectCount(queryWrapper) == 0, "商品名称重复");
    }

    private void validateItemSkuName(List<ItemSkuDto> skuVoList) {
         Assert.isTrue(
             skuVoList.stream().map(ItemSkuDto::getSkuName).distinct().count() == skuVoList.size(),
             "商品规格重复"
         );
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
