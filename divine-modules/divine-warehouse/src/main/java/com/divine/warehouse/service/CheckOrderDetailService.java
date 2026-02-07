package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.entity.CheckOrderDetail;
import com.divine.warehouse.domain.vo.CheckOrderDetailVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author zcc
 * @date 2024-08-13
 */
public interface CheckOrderDetailService {


    /**
     * 查询库存盘点单据详情
     */
    CheckOrderDetailVo queryById(Long id);

    /**
     * 查询库存盘点单据详情列表
     */
    PageInfoRes<CheckOrderDetailVo> queryPageList(CheckOrderDetailDto bo, BasePage basePage);


    /**
     * 查询库存盘点单据详情列表
     */
    List<CheckOrderDetailVo> queryList(CheckOrderDetailDto bo);


    /**
     * 新增库存盘点单据详情
     */
    void insertByBo(CheckOrderDetailDto bo);

    /**
     * 修改库存盘点单据详情
     */
    void updateByBo(CheckOrderDetailDto bo);

    /**
     * 批量删除库存盘点单据详情
     */
    void deleteByIds(Collection<Long> ids);

    void saveDetails(List<CheckOrderDetail> list);

    List<CheckOrderDetailVo> queryByCheckOrderId(Long checkOrderId);
}
