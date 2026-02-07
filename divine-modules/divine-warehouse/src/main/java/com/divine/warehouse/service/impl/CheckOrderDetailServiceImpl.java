package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.entity.CheckOrderDetail;
import com.divine.warehouse.domain.vo.CheckOrderDetailVo;
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
import java.util.Map;

/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author zcc
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
    public CheckOrderDetailVo queryById(Long id) {
        return checkOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询库存盘点单据详情列表
     */
    @Override
    public PageInfoRes<CheckOrderDetailVo> queryPageList(CheckOrderDetailDto bo, BasePage basePage) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = buildQueryWrapper(bo);
        Page<CheckOrderDetailVo> result = checkOrderDetailMapper.selectVoPage(basePage.build(), lqw);
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
    public List<CheckOrderDetailVo> queryList(CheckOrderDetailDto bo) {
        LambdaQueryWrapper<CheckOrderDetail> lqw = buildQueryWrapper(bo);
        return checkOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CheckOrderDetail> buildQueryWrapper(CheckOrderDetailDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<CheckOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderId() != null, CheckOrderDetail::getOrderId, bo.getOrderId());
        lqw.eq(bo.getSkuId() != null, CheckOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, CheckOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getCheckQuantity() != null, CheckOrderDetail::getCheckQuantity, bo.getCheckQuantity());
        lqw.eq(bo.getWarehouseId() != null, CheckOrderDetail::getWarehouseId, bo.getWarehouseId());
        lqw.apply(bo.getHaveProfitAndLoss() != null && bo.getHaveProfitAndLoss(), "quantity != check_quantity");
        return lqw;
    }

    /**
     * 新增库存盘点单据详情
     */
    @Override
    public void insertByBo(CheckOrderDetailDto bo) {
        CheckOrderDetail add = MapstructUtils.convert(bo, CheckOrderDetail.class);
        checkOrderDetailMapper.insert(add);
    }

    /**
     * 修改库存盘点单据详情
     */
    @Override
    public void updateByBo(CheckOrderDetailDto bo) {
        CheckOrderDetail update = MapstructUtils.convert(bo, CheckOrderDetail.class);
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
    public List<CheckOrderDetailVo> queryByCheckOrderId(Long checkOrderId) {
        CheckOrderDetailDto bo = new CheckOrderDetailDto();
        bo.setOrderId(checkOrderId);
        List<CheckOrderDetailVo> details = queryList(bo);
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
