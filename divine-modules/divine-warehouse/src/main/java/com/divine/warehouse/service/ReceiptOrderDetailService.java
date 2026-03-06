package com.divine.warehouse.service;

import com.divine.warehouse.domain.dto.ReceiptOrderDetailDto;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import com.divine.warehouse.domain.vo.ReceiptDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * 入库单详情Service业务层处理
 *
 * @author yisl
 * @date 2024-07-19
 */
public interface ReceiptOrderDetailService{

    /**
     * 查询入库单详情
     */
    ReceiptOrderDetailVO queryById(Long id);

    /**
     * 查询入库单详情列表
     */
    PageInfoRes<ReceiptDetailVO> queryPageList(Long receiptId, BasePage basePage);

    /**
     * 查询入库单详情列表
     */
    List<ReceiptOrderDetailVO> queryList(ReceiptOrderDetailDto dto);

    /**
     * 新增入库单详情
     */
    void insertByBo(ReceiptOrderDetailDto dto);

    /**
     * 修改入库单详情
     */
    void updateByBo(ReceiptOrderDetailDto dto);

    /**
     * 批量删除入库单详情
     */
    void deleteByIds(Collection<Long> ids);

    /**
     * 根据入库单id删除入库单详情
     */
    void deleteByReceiptOrderId(@NotNull Long receiptOrderId);

    void saveDetails(List<ReceiptOrderDetail> list);

    /**
     * 查询入库单明细
     * @param receiptOrderId
     * @return
     */
    List<ReceiptOrderDetailVO> queryByReceiptOrderId(Long receiptOrderId);
}
