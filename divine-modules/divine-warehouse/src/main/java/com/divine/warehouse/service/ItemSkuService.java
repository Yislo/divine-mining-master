package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.vo.BaseOrderDetailVo;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemSkuService{

    /**
     * 查询sku信息
     */
    ItemSkuMapVo queryItemSkuMapVo(Long id);

    ItemSkuVo queryById(Long id);

    /**
     * 查询sku信息列表，用于出入库的选择组件
     */
    PageInfoRes<ItemSkuMapVo> queryPageList(ItemSkuDto bo, BasePage basePage);

    /**
     * 查询sku信息列表
     */
    List<ItemSkuVo> queryList(ItemSkuDto bo);

    /**
     * 新增sku信息
     */
    Boolean insertByBo(ItemSkuDto bo) ;

    /**
     * 修改sku信息
     */
    Boolean updateByBo(ItemSkuDto bo) ;

    void deleteById(Long id) ;

    /**
     * 批量删除sku信息
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 批量保存商品sku
     * @param sku    商品sku
     */
    @Transactional
    void saveOrUpdateBatchByBo(List<ItemSkuDto> sku) ;

    void setItemId(List<ItemSkuDto> itemSkuList, Long itemId);

    /**
     * 查询sku列表
     *
     * @param id 商品id
     */
    List<ItemSkuVo> queryByItemId(Long id) ;

    Map<Long, ItemSkuMapVo> queryItemSkuMapVosByIds(Set<Long> skuIds);

    void setItemSkuMap(List<? extends BaseOrderDetailVo> details);
}
