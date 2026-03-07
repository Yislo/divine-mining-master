package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.entity.CheckOrderDetail;
import com.divine.warehouse.domain.vo.CheckOrderDetailVO;
import com.divine.warehouse.mapper.CheckOrderDetailMapper;
import com.divine.warehouse.service.CheckOrderDetailService;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author yisl
 * @date 2024-08-13
 */
@RequiredArgsConstructor
@Service
public class CheckOrderDetailServiceImpl extends ServiceImpl<CheckOrderDetailMapper, CheckOrderDetail> implements CheckOrderDetailService {

    private final CheckOrderDetailMapper checkOrderDetailMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询库存盘点单据详情
     */
    @Override
    public CheckOrderDetailVO queryById(Long id) {
        return checkOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询库存盘点单据详情列表
     */
    @Override
    public PageInfoRes<CheckOrderDetailVO> queryPageList(CheckOrderDetailDto dto, BasePage basePage) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = buildQueryWrapper(dto);
        Page<CheckOrderDetailVO> result = checkOrderDetailMapper.selectVoPage(basePage.build(), lqw);
        if (CollUtil.isEmpty(result.getRecords())) {
            return PageInfoRes.build(result);
        }
        itemSkuService.setItemSkuMap(result.getRecords());
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存盘点单据详情列表
     */
    @Override
    public List<CheckOrderDetailVO> queryList(CheckOrderDetailDto dto) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = buildQueryWrapper(dto);
        return checkOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CheckOrderDetail> buildQueryWrapper(CheckOrderDetailDto dto) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getCheckId() != null, CheckOrderDetail::getCheckId, dto.getCheckId());
        lqw.eq(dto.getSkuId() != null, CheckOrderDetail::getSkuId, dto.getSkuId());
//        lqw.eq(dto.getQuantity() != null, CheckOrderDetail::getQuantity, dto.getQuantity());
        lqw.eq(dto.getCheckQuantity() != null, CheckOrderDetail::getCheckQuantity, dto.getCheckQuantity());
        lqw.eq(dto.getWarehouseId() != null, CheckOrderDetail::getWarehouseId, dto.getWarehouseId());
        lqw.apply(dto.getHaveProfitAndLoss() != null && dto.getHaveProfitAndLoss(), "quantity != check_quantity");
        return lqw;
    }

    /**
     * 新增库存盘点单据详情
     */
    @Override
    public void insertByBo(CheckOrderDetailDto dto) {
        CheckOrderDetail add = MapstructUtils.convert(dto, CheckOrderDetail.class);
        checkOrderDetailMapper.insert(add);
    }

    /**
     * 修改库存盘点单据详情
     */
    @Override
    public void updateByBo(CheckOrderDetailDto dto) {
        CheckOrderDetail update = MapstructUtils.convert(dto, CheckOrderDetail.class);
        checkOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除库存盘点单据详情
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        checkOrderDetailMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void saveDetails(List<CheckOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    @Override
    public List<CheckOrderDetailVO> queryByCheckOrderId(Long checkOrderId) {
        CheckOrderDetailDto dto = new CheckOrderDetailDto();
        dto.setCheckId(checkOrderId);
        List<CheckOrderDetailVO> details = queryList(dto);
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
