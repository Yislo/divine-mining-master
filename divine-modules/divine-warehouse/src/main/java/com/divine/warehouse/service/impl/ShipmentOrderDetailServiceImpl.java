package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVo;
import com.divine.warehouse.mapper.ShipmentOrderDetailMapper;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.warehouse.service.ShipmentOrderDetailService;
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
 * 出库单详情Service业务层处理
 *
 * @author zcc
 * @date 2024-08-01
 */
@RequiredArgsConstructor
@Service
public class ShipmentOrderDetailServiceImpl extends ServiceImpl<ShipmentOrderDetailMapper, ShipmentOrderDetail> implements ShipmentOrderDetailService {

    private final ShipmentOrderDetailMapper shipmentOrderDetailMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询出库单详情
     */
    @Override
    public ShipmentOrderDetailVo queryById(Long id){
        return shipmentOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询出库单详情列表
     */
    @Override
    public PageInfoRes<ShipmentOrderDetailVo> queryPageList(ShipmentOrderDetailDto bo, BasePage basePage) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = buildQueryWrapper(bo);
        Page<ShipmentOrderDetailVo> result = shipmentOrderDetailMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询出库单详情列表
     */
    @Override
    public List<ShipmentOrderDetailVo> queryList(ShipmentOrderDetailDto bo) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = buildQueryWrapper(bo);
        return shipmentOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShipmentOrderDetail> buildQueryWrapper(ShipmentOrderDetailDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderId() != null, ShipmentOrderDetail::getOrderId, bo.getOrderId());
        lqw.eq(bo.getSkuId() != null, ShipmentOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, ShipmentOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getAmount() != null, ShipmentOrderDetail::getAmount, bo.getAmount());
        lqw.eq(bo.getWarehouseId() != null, ShipmentOrderDetail::getWarehouseId, bo.getWarehouseId());
        return lqw;
    }

    /**
     * 新增出库单详情
     */
    @Override
    public void insertByBo(ShipmentOrderDetailDto bo) {
        ShipmentOrderDetail add = MapstructUtils.convert(bo, ShipmentOrderDetail.class);
        shipmentOrderDetailMapper.insert(add);
    }

    /**
     * 修改出库单详情
     */
    @Override
    public void updateByBo(ShipmentOrderDetailDto bo) {
        ShipmentOrderDetail update = MapstructUtils.convert(bo, ShipmentOrderDetail.class);
        shipmentOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除出库单详情
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        shipmentOrderDetailMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void saveDetails(List<ShipmentOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    @Override
    public List<ShipmentOrderDetailVo> queryByShipmentOrderId(Long shipmentOrderId) {
        ShipmentOrderDetailDto bo = new ShipmentOrderDetailDto();
        bo.setOrderId(shipmentOrderId);
        List<ShipmentOrderDetailVo> details = queryList(bo);
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
