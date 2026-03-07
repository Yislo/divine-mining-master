package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVO;
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
 * @author yisl
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
    public ShipmentOrderDetailVO queryById(Long id){
        return shipmentOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询出库单详情列表
     */
    @Override
    public PageInfoRes<ShipmentOrderDetailVO> queryPageList(ShipmentOrderDetailDto dto, BasePage basePage) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = buildQueryWrapper(dto);
        Page<ShipmentOrderDetailVO> result = shipmentOrderDetailMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询出库单详情列表
     */
    @Override
    public List<ShipmentOrderDetailVO> queryList(ShipmentOrderDetailDto dto) {
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = buildQueryWrapper(dto);
        return shipmentOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShipmentOrderDetail> buildQueryWrapper(ShipmentOrderDetailDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<ShipmentOrderDetail> lqw = Wrappers.lambdaQuery();
//        lqw.eq(dto.getOrderId() != null, ShipmentOrderDetail::getReceiptNo, dto.getOrderId());
        lqw.eq(dto.getSkuId() != null, ShipmentOrderDetail::getSkuId, dto.getSkuId());
//        lqw.eq(dto.getQuantity() != null, ShipmentOrderDetail::getQuantity, dto.getQuantity());
//        lqw.eq(dto.getAmount() != null, ShipmentOrderDetail::getAmount, dto.getAmount());
        lqw.eq(dto.getWarehouseId() != null, ShipmentOrderDetail::getWarehouseId, dto.getWarehouseId());
        return lqw;
    }

    /**
     * 新增出库单详情
     */
    @Override
    public void insertByBo(ShipmentOrderDetailDto dto) {
        ShipmentOrderDetail add = MapstructUtils.convert(dto, ShipmentOrderDetail.class);
        shipmentOrderDetailMapper.insert(add);
    }

    /**
     * 修改出库单详情
     */
    @Override
    public void updateByBo(ShipmentOrderDetailDto dto) {
        ShipmentOrderDetail update = MapstructUtils.convert(dto, ShipmentOrderDetail.class);
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
    public List<ShipmentOrderDetailVO> queryByShipmentOrderId(Long shipmentOrderId) {
        ShipmentOrderDetailDto dto = new ShipmentOrderDetailDto();
        dto.setShipmentId(shipmentOrderId);
        List<ShipmentOrderDetailVO> details = queryList(dto);
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
