package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.entity.ItemSku;
import com.divine.warehouse.domain.vo.BaseOrderDetailVo;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.warehouse.mapper.ItemSkuMapper;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.constant.HttpStatus;
import com.divine.common.core.exception.ServiceException;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Service
@Slf4j
public class ItemSkuServiceImpl extends ServiceImpl<ItemSkuMapper, ItemSku> implements ItemSkuService {


    private final ItemSkuMapper itemSkuMapper;
    private final InventoryService inventoryService;

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
    public PageInfoRes<ItemSkuMapVo> queryPageList(ItemSkuDto bo, BasePage basePage) {
        //开始查sku
        IPage<ItemSkuMapVo> result = itemSkuMapper.selectByBo(basePage.build(), bo);
        return PageInfoRes.build(result);
    }

    /**
     * 查询sku信息列表
     */
    @Override
    public List<ItemSkuVo> queryList(ItemSkuDto bo) {
        LambdaQueryWrapper<ItemSku> lqw = buildQueryWrapper(bo);
        return itemSkuMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ItemSku> buildQueryWrapper(ItemSkuDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ItemSku> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getSkuName()), ItemSku::getSkuName, bo.getSkuName());
        lqw.eq(bo.getItemId() != null, ItemSku::getItemId, bo.getItemId());
        lqw.eq(StrUtil.isNotBlank(bo.getBarcode()), ItemSku::getBarcode, bo.getBarcode());
        lqw.orderByDesc(ItemSku::getItemId);
        return lqw;
    }

    /**
     * 新增sku信息
     */
    @Override
    public Boolean insertByBo(ItemSkuDto bo) {
        ItemSku add = MapstructUtils.convert(bo, ItemSku.class);
        return itemSkuMapper.insert(add) > 0;
    }

    /**
     * 修改sku信息
     */
    @Override
    public Boolean updateByBo(ItemSkuDto bo) {
        ItemSku update = MapstructUtils.convert(bo, ItemSku.class);
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

        if(queryByItemId(itemSku.getItemId()).size() <= 1){
            throw new ServiceException("删除失败", HttpStatus.CONFLICT,"至少包含一个商品规格！");
        }
        // 校验库存是否已关联
        if (inventoryService.existsBySkuIds(List.of(id))) {
            throw new ServiceException("删除失败", HttpStatus.CONFLICT,"该规格已有业务关联，无法删除！");
        }
    }

    private void validateSkuIdsBeforeDelete(Collection<Long> skuIds) {
        if (inventoryService.existsBySkuIds(skuIds)) {
            throw new ServiceException("删除失败", HttpStatus.CONFLICT,"该商品已有业务关联，无法删除！");
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
     * 批量保存商品sku
     * @param sku    商品sku
     */
    @Override
    @Transactional
    public void saveOrUpdateBatchByBo(List<ItemSkuDto> sku) {
        List<ItemSku> itemSkuList = MapstructUtils.convert(sku, ItemSku.class);
        saveOrUpdateBatch(itemSkuList);
    }

    public void setItemId(List<ItemSkuDto> itemSkuList, Long itemId) {
        for (ItemSkuDto itemSkuDto : itemSkuList) {
            if (StrUtil.isBlank(itemSkuDto.getBarcode())) {
                itemSkuDto.setItemId(itemId);
            }
        }
    }

    /**
     * 查询sku列表
     *
     * @param id 商品id
     */
    @Override
    public List<ItemSkuVo> queryByItemId(Long id) {
        LambdaQueryWrapper<ItemSku> lqw = Wrappers.lambdaQuery();
        lqw.eq(ItemSku::getItemId, id);
        return itemSkuMapper.selectVoList(lqw);
    }

    @Override
    public Map<Long, ItemSkuMapVo> queryItemSkuMapVosByIds(Set<Long> skuIds){
        return itemSkuMapper.queryItemSkuMapVos(skuIds).stream()
            .collect(Collectors.toMap(ItemSkuMapVo::getSkuId, Function.identity()));
    }

    @Override
    public void setItemSkuMap(List<? extends BaseOrderDetailVo> details){
        if (CollUtil.isNotEmpty(details)) {
            Set<Long> skuIds = details
                .stream()
                .map(BaseOrderDetailVo::getSkuId)
                .collect(Collectors.toSet());

            Map<Long, ItemSkuMapVo> itemSkuMap = this.queryItemSkuMapVosByIds(skuIds);

            details.forEach(detail -> {
                    ItemSkuMapVo vo = itemSkuMap.get(detail.getSkuId());
                    detail.setItemSku(vo.getItemSku());
                    detail.setItem(vo.getItem());
            });
        }
    }
}
