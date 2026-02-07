package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ReceiptOrderDto;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.*;

/**
 * 入库单Service业务层处理
 *
 * @author zcc
 * @date 2024-07-19
 */
public interface ReceiptOrderService {


    /**
     * 查询入库单
     */
    ReceiptOrderVo queryById(Long id);

    Long queryIdByOrderNo(String orderNo);

    /**
     * 查询入库单列表
     */
    PageInfoRes<ReceiptOrderVo> queryPageList(ReceiptOrderDto bo, BasePage basePage);

    /**
     * 查询入库单列表
     */
    List<ReceiptOrderVo> queryList(ReceiptOrderDto bo) ;


    /**
     * 暂存入库单
     */
    Long insertByBo(ReceiptOrderDto bo);

    /**
     * 入库：
     * 1.校验
     * 2.保存入库单和入库单明细
     * 3.保存库存明细
     * 4.增加库存
     * 5.保存库存记录
     */
    void receive(ReceiptOrderDto bo);

    /**
     * 修改入库单
     */
    void updateByBo(ReceiptOrderDto bo) ;

    /**
     * 入库单作废
     * @param id
     */
    void editToInvalid(Long id) ;

    /**
     * 删除入库单
     */
    void deleteById(Long id) ;

    /**
     * 批量删除入库单
     */
    void deleteByIds(Collection<Long> ids);

    void validateReceiptOrderNo(String receiptOrderNo) ;
}
