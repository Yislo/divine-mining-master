package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.MerchantDto;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.Collection;
import java.util.List;

/**
 * 往来单位Service业务层处理
 *
 * @author zcc
 * @date 2024-07-16
 */
public interface MerchantService {

    /**
     * 查询往来单位
     */
    MerchantVo queryById(Long id);

    /**
     * 查询往来单位列表
     */
    PageInfoRes<MerchantVo> queryPageList(MerchantDto bo, BasePage basePage) ;

    /**
     * 查询往来单位列表
     */
    List<MerchantVo> queryList(MerchantDto bo) ;

    /**
     * 新增往来单位
     */
    void insertByBo(MerchantDto bo) ;

    /**
     * 修改往来单位
     */
    void updateByBo(MerchantDto bo);

    /**
     * 删除往来单位
     */
    void deleteById(Long id);

    /**
     * 批量删除往来单位
     */
    void deleteByIds(Collection<Long> ids);
}
