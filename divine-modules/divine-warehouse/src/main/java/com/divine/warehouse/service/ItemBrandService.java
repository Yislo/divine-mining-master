package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ItemBrandDto;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.warehouse.domain.vo.ItemBrandVo;

import java.util.List;

/**
 * 商品品牌Service业务层处理
 *
 * @author zcc
 * @date 2024-07-30
 */
public interface ItemBrandService {


    /**
     * 查询商品品牌
     */
    ItemBrandVo queryById(Long id);

    /**
     * 查询商品品牌列表
     */
    PageInfoRes<ItemBrandVo> queryPageList(ItemBrandDto bo, BasePage basePage) ;

    /**
     * 查询商品品牌列表
     */
    List<ItemBrandVo> queryList(ItemBrandDto bo) ;

    /**
     * 新增商品品牌
     */
    void insertByBo(ItemBrandDto bo) ;

    /**
     * 修改商品品牌
     */
    void updateByBo(ItemBrandDto bo);

    /**
     * 批量删除商品品牌
     */
    void deleteById(Long id);

}
