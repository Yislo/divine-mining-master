package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVo;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.warehouse.mapper.ShipmentOrderMapper;
import com.divine.warehouse.service.InventoryHistoryService;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.ShipmentOrderDetailService;
import com.divine.warehouse.service.ShipmentOrderService;
import com.divine.common.core.constant.HttpStatus;
import com.divine.common.core.constant.ServiceConstants;
import com.divine.common.core.exception.ServiceException;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 出库单Service业务层处理
 *
 * @author zcc
 * @date 2024-08-01
 */
@RequiredArgsConstructor
@Service
public class ShipmentOrderServiceImpl implements ShipmentOrderService {

    private final ShipmentOrderMapper shipmentOrderMapper;
    private final ShipmentOrderDetailService shipmentOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryHistoryService inventoryHistoryService;

    /**
     * 查询出库单
     */
    @Override
    public ShipmentOrderVo queryById(Long id){
        ShipmentOrderVo shipmentOrderVo = shipmentOrderMapper.selectVoById(id);
        if (shipmentOrderVo == null) {
            throw new BusinessException("出库单不存在");
        }
        shipmentOrderVo.setDetails(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderVo.getId()));
        return shipmentOrderVo;
    }

    /**
     * 查询出库单列表
     */
    @Override
    public PageInfoRes<ShipmentOrderVo> queryPageList(ShipmentOrderDto bo, BasePage basePage) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(bo);
        Page<ShipmentOrderVo> result = shipmentOrderMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询出库单列表
     */
    @Override
    public List<ShipmentOrderVo> queryList(ShipmentOrderDto bo) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(bo);
        return shipmentOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShipmentOrder> buildQueryWrapper(ShipmentOrderDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ShipmentOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNo()), ShipmentOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getOptType() != null, ShipmentOrder::getOptType, bo.getOptType());
        lqw.eq(StringUtils.isNotBlank(bo.getOrderNo()), ShipmentOrder::getOrderNo, bo.getOrderNo());
        lqw.eq(bo.getMerchantId() != null, ShipmentOrder::getMerchantId, bo.getMerchantId());
        lqw.eq(bo.getTotalAmount() != null, ShipmentOrder::getTotalAmount, bo.getTotalAmount());
        lqw.eq(bo.getTotalQuantity() != null, ShipmentOrder::getTotalQuantity, bo.getTotalQuantity());
        lqw.eq(bo.getOrderStatus() != null, ShipmentOrder::getOrderStatus, bo.getOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存出库单
     */
    @Override
    @Transactional
    public Long insertByBo(ShipmentOrderDto bo) {
        // 校验出库单号唯一性
        validateShipmentOrderNo(bo.getOrderNo());
        // 创建出库单
        ShipmentOrder add = MapstructUtils.convert(bo, ShipmentOrder.class);
        shipmentOrderMapper.insert(add);
        bo.setId(add.getId());
        List<ShipmentOrderDetailDto> detailBoList = bo.getDetails();
        List<ShipmentOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ShipmentOrderDetail.class);
        addDetailList.forEach(it -> it.setOrderId(add.getId()));
        shipmentOrderDetailService.saveDetails(addDetailList);
        return add.getId();
    }

    @Override
    public void validateShipmentOrderNo(String shipmentOrderNo) {
        LambdaQueryWrapper<ShipmentOrder> receiptOrderLqw = Wrappers.lambdaQuery();
        receiptOrderLqw.eq(ShipmentOrder::getOrderNo, shipmentOrderNo);
        ShipmentOrder shipmentOrder = shipmentOrderMapper.selectOne(receiptOrderLqw);
        Assert.isNull(shipmentOrder, "出库单号重复，请手动修改");
    }


    /**
     * 修改出库单
     */
    @Override
    @Transactional
    public void updateByBo(ShipmentOrderDto bo) {
        // 更新出库单
        ShipmentOrder update = MapstructUtils.convert(bo, ShipmentOrder.class);
        shipmentOrderMapper.updateById(update);
        // 保存出库单明细
        List<ShipmentOrderDetail> detailList = MapstructUtils.convert(bo.getDetails(), ShipmentOrderDetail.class);

        //需要考虑detail删除
        List<ShipmentOrderDetailVo> dbList = shipmentOrderDetailService.queryByShipmentOrderId(bo.getId());
        Set<Long> ids = detailList.stream().filter(it -> it.getId() != null).map(it -> it.getId()).collect(Collectors.toSet());
        List<ShipmentOrderDetailVo> delList = dbList.stream().filter(it -> !ids.contains(it.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(delList)) {
            shipmentOrderDetailService.deleteByIds(delList.stream().map(it->it.getId()).collect(Collectors.toList()));
        }
        detailList.forEach(it -> it.setOrderId(bo.getId()));
        shipmentOrderDetailService.saveDetails(detailList);
    }

    /**
     * 批量删除出库单
     */
    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        shipmentOrderMapper.deleteById(id);
    }

    @Override
    public void validateIdBeforeDelete(Long id) {
        ShipmentOrderVo shipmentOrderVo = queryById(id);
        if (shipmentOrderVo == null) {
            throw new BusinessException("出库单不存在");
        }
        if (ServiceConstants.ShipmentOrderStatus.FINISH.equals(shipmentOrderVo.getOrderStatus())) {
            throw new ServiceException("删除失败", HttpStatus.CONFLICT,"出库单【" + shipmentOrderVo.getOrderNo() + "】已出库，无法删除！");
        }
    }

    /**
     * 出库
     * @param bo
     */
    @Override
    @Transactional
    public void shipment(ShipmentOrderDto bo) {
        // 1.校验商品明细不能为空！
        validateBeforeShipment(bo);
        // 2. 保存入库单和入库单明细
        if (Objects.isNull(bo.getId())) {
            insertByBo(bo);
        } else {
            updateByBo(bo);
        }
        // 3.更新库存：Inventory表
        inventoryService.subtract(bo.getDetails());

        // 4.创建库存记录
        inventoryHistoryService.saveInventoryHistory(bo,ServiceConstants.InventoryHistoryOrderType.SHIPMENT,false);
    }


    private void validateBeforeShipment(ShipmentOrderDto bo) {
        if (CollUtil.isEmpty(bo.getDetails())) {
            throw new BusinessException("商品明细不能为空！");
        }
    }

    @Override
    public Long queryIdByOrderNo(String orderNo) {
        ShipmentOrderVo shipmentOrder = shipmentOrderMapper.selectVoOne(new QueryWrapper<ShipmentOrder>().eq("order_no",orderNo));
        return shipmentOrder != null ? shipmentOrder.getId() : null;
    }
}
