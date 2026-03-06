package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.entity.BaseOrderDetail;
import com.divine.warehouse.domain.entity.ShipmentOrder;
import com.divine.warehouse.domain.entity.ShipmentOrderDetail;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.BaseOrderDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.warehouse.domain.vo.ShipmentOrderDetailVO;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.warehouse.mapper.ShipmentOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
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
 * @author yisl
 * @date 2024-08-01
 */
@RequiredArgsConstructor
@Service
public class ShipmentOrderServiceImpl implements ShipmentOrderService {

    private final ShipmentOrderMapper shipmentOrderMapper;
    private final ShipmentOrderDetailService shipmentOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryHistoryService inventoryHistoryService;
    private final CommonService commonService;
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询出库单
     */
    @Override
    public ShipmentOrderVo queryById(Long id) {
        ShipmentOrderVo shipmentOrderVo = shipmentOrderMapper.selectVoById(id);
        if (shipmentOrderVo == null) {
            throw new com.divine.common.core.exception.base.BusinessException("出库单不存在");
        }
        shipmentOrderVo.setDetails(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderVo.getId()));
        return shipmentOrderVo;
    }

    /**
     * 查询出库单列表
     */
    @Override
    public PageInfoRes<ShipmentOrderVo> queryPageList(ShipmentOrderDto dto, BasePage basePage) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(dto);
        Page<ShipmentOrderVo> result = shipmentOrderMapper.selectVoPage(basePage.build(), lqw);
        // 获取仓库信息
        List<ShipmentOrderVo> records = result.getRecords();
        List<Long> wareIds = records.stream().map(ShipmentOrderVo::getWarehouseId).toList();
        if (CollectionUtil.isNotEmpty(wareIds)){
            List<Warehouse> warehouses = warehouseMapper.selectList(new LambdaQueryWrapper<>(Warehouse.class)
                .in(Warehouse::getId, wareIds));
            Map<Long, String> warehousesMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));
            records.forEach(r-> r.setWarehouseName(warehousesMap.get(r.getWarehouseId())));
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询出库单列表
     */
    @Override
    public List<ShipmentOrderVo> queryList(ShipmentOrderDto dto) {
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(dto);
        return shipmentOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ShipmentOrder> buildQueryWrapper(ShipmentOrderDto dto) {
        LambdaQueryWrapper<ShipmentOrder> lqw = Wrappers.lambdaQuery();
//        lqw.eq(StringUtils.isNotBlank(dto.getOrderNo()), ShipmentOrder::getOrderNo, dto.getOrderNo());
        lqw.eq(dto.getOptType() != null, ShipmentOrder::getOptType, dto.getOptType());
//        lqw.eq(StringUtils.isNotBlank(dto.getOrderNo()), ShipmentOrder::getOrderNo, dto.getOrderNo());
        lqw.eq(dto.getRecipient() != null, ShipmentOrder::getRecipient, dto.getRecipient());
        lqw.eq(dto.getTotalPrice() != null, ShipmentOrder::getTotalPrice, dto.getTotalPrice());
        lqw.eq(dto.getTotalQuantity() != null, ShipmentOrder::getTotalQuantity, dto.getTotalQuantity());
//        lqw.eq(dto.getOrderStatus() != null, ShipmentOrder::getOrderStatus, dto.getOrderStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存出库单
     */
    @Override
    @Transactional
    public Long insertByBo(ShipmentOrderDto dto) {
        // 创建出库单
        String shipmentNo = commonService.getNo(InventoryTypeEnum.SHIPMENT.getCode());
        dto.setBizNo(shipmentNo);
        //组装数据保存
        ShipmentOrder shipmentOrder = MapstructUtils.convert(dto, ShipmentOrder.class);
        shipmentOrder.setShipmentNo(shipmentNo);
        shipmentOrder.setShipmentStatus(InventoryStatusEnum.FINISH.getCode());
        shipmentOrderMapper.insert(shipmentOrder);
        List<ShipmentOrderDetailDto> detailBoList = dto.getDetails();
        List<ShipmentOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ShipmentOrderDetail.class);
        addDetailList.forEach(it -> it.setShipmentId(shipmentOrder.getId()));
        shipmentOrderDetailService.saveDetails(addDetailList);
        return shipmentOrder.getId();
    }

    /**
     * 修改出库单
     */
    @Override
    @Transactional
    public void updateByBo(ShipmentOrderDto dto) {
        // 更新出库单
        ShipmentOrder shipmentOrder = MapstructUtils.convert(dto, ShipmentOrder.class);
        shipmentOrder.setShipmentStatus(InventoryStatusEnum.FINISH.getCode());
        shipmentOrderMapper.updateById(shipmentOrder);
        // 保存出库单明细
        List<ShipmentOrderDetail> detailList = MapstructUtils.convert(dto.getDetails(), ShipmentOrderDetail.class);

        //需要考虑detail删除
        List<ShipmentOrderDetailVO> dbList = shipmentOrderDetailService.queryByShipmentOrderId(dto.getId());
        Set<Long> ids = detailList.stream().map(BaseOrderDetail::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<ShipmentOrderDetailVO> delList = dbList.stream().filter(it -> !ids.contains(it.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(delList)) {
            shipmentOrderDetailService.deleteByIds(delList.stream().map(BaseOrderDetailVO::getId).collect(Collectors.toList()));
        }
        detailList.forEach(it -> it.setShipmentId(dto.getId()));
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
            throw new com.divine.common.core.exception.base.BusinessException("出库单不存在");
        }
        if (InventoryStatusEnum.FINISH.getCode().equals(shipmentOrderVo.getShipmentStatus())) {
            throw new BusinessException("出库单【" + shipmentOrderVo.getShipmentNo() + "】已出库，无法删除！");
        }
    }

    /**
     * 出库
     *
     * @param dto
     */
    @Override
    @Transactional
    public void shipment(ShipmentOrderDto dto) {
        // 1.校验物品明细不能为空！
        validateBeforeShipment(dto);
        // 2. 保存入库单和入库单明细
        if (Objects.isNull(dto.getId())) {
            insertByBo(dto);
        } else {
            updateByBo(dto);
        }
        // 3.更新库存：Inventory表
        inventoryService.subtract(dto.getDetails());

        // 4.创建库存记录
        inventoryHistoryService.saveInventoryHistory(dto, InventoryTypeEnum.SHIPMENT.getType(), false);
    }


    private void validateBeforeShipment(ShipmentOrderDto dto) {
        if (CollUtil.isEmpty(dto.getDetails())) {
            throw new com.divine.common.core.exception.base.BusinessException("物品明细不能为空！");
        }
    }

    @Override
    public Long queryIdByOrderNo(String orderNo) {
        ShipmentOrderVo shipmentOrder = shipmentOrderMapper.selectVoOne(new QueryWrapper<ShipmentOrder>().eq("order_no", orderNo));
        return shipmentOrder != null ? shipmentOrder.getId() : null;
    }
}
