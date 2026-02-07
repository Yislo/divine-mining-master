package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ItemDto;
import com.divine.warehouse.domain.vo.ItemVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.List;

public interface ItemService {


    /**
     * 查询物料
     */
    ItemVo queryById(Long id);

    /**
     * 查询物料
     *
     * @param itemIds ids
     */
    List<ItemVo> queryById(List<Long> itemIds);

    /**
     * 查询物料列表
     */
    PageInfoRes<ItemVo> queryPageList(ItemDto bo, BasePage basePage) ;

    /**
     * 查询物料列表
     */
    List<ItemVo> queryList(ItemDto bo);

    /**
     * 新增物料
     *
     * @param bo
     */
    void insertByForm(ItemDto bo) ;

    /**
     * 修改物料
     *
     * @param bo
     */
    void updateByForm(ItemDto bo) ;

    /**
     * 批量删除物料
     */
    void deleteById(Long id);

}
