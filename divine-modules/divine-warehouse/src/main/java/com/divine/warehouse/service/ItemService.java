package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ItemDto;
import com.divine.warehouse.domain.vo.ItemInfoVo;
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
    PageInfoRes<ItemVo> queryPageList(ItemDto dto, BasePage basePage);

    /**
     * 查询物料列表
     */
    List<ItemVo> queryList(ItemDto dto);

    /**
     * 查询物品详情
     *
     * @param dto
     * @return
     */
    List<ItemInfoVo>  queryItemInfo(ItemDto dto);

    /**
     * 新增物料
     *
     * @param dto
     */
    void insertByForm(ItemDto dto);

    /**
     * 修改物料
     *
     * @param dto
     */
    void updateByForm(ItemDto dto);

    /**
     * 查询物品信息
     */
    void deleteById(Long id);

}
