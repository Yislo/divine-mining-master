package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.MovementOrderDto;
import com.divine.warehouse.domain.entity.MovementOrder;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.CheckOrderVo;
import com.divine.warehouse.domain.vo.MovementOrderVo;
import com.divine.warehouse.mapper.MovementOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.google.api.client.util.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 移库单Service业务层处理
 *
 * @author yisl
 * @date 2024-08-09
 */
@RequiredArgsConstructor
@Service
public class MovementOrderServiceImpl implements MovementOrderService {

    private final MovementOrderMapper movementOrderMapper;
    private final MovementOrderDetailService movementOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryHistoryService inventoryHistoryService;
    private final CommonService commonService;
    private final WarehouseMapper warehouseMapper;


    /**
     * 查询移库单
     */
    @Override
    public MovementOrderVo queryById(Long id) {
        MovementOrderVo movementOrderVo = movementOrderMapper.selectVoById(id);
        if (movementOrderVo == null) {
            throw new com.divine.common.core.exception.base.BusinessException("移库单不存在");
        }
        movementOrderVo.setDetails(movementOrderDetailService.queryByMovementOrderId(id));
        return movementOrderVo;
    }

    /**
     * 查询移库单列表
     */
    @Override
    public PageInfoRes<MovementOrderVo> queryPageList(MovementOrderDto dto, BasePage basePage) {
        LambdaQueryWrapper<MovementOrder> lqw = buildQueryWrapper(dto);
        Page<MovementOrderVo> result = movementOrderMapper.selectVoPage(basePage.build(), lqw);
        // 获取仓库信息
        List<MovementOrderVo> records = result.getRecords();
        Set<Long> wareIds = records.stream().map(MovementOrderVo::getSourceWarehouseId).collect(Collectors.toSet());
        wareIds.addAll(records.stream().map(MovementOrderVo::getTargetWarehouseId).distinct().toList());
        if (CollectionUtil.isNotEmpty(wareIds)){
            List<Warehouse> warehouses = warehouseMapper.selectList(new LambdaQueryWrapper<>(Warehouse.class)
                .in(Warehouse::getId, wareIds));
            Map<Long, String> warehousesMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));
            records.forEach(r->{
                r.setSourceWarehouseName(warehousesMap.get(r.getSourceWarehouseId()));
                r.setTargetWarehouseName(warehousesMap.get(r.getTargetWarehouseId()));
            });
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询移库单列表
     */
    @Override
    public List<MovementOrderVo> queryList(MovementOrderDto dto) {
        LambdaQueryWrapper<MovementOrder> lqw = buildQueryWrapper(dto);
        return movementOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MovementOrder> buildQueryWrapper(MovementOrderDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<MovementOrder> lqw = Wrappers.lambdaQuery();
//        lqw.eq(StringUtils.isNotBlank(dto.getOrderNo()), MovementOrder::getOrderNo, dto.getOrderNo());
        lqw.eq(dto.getSourceWarehouseId() != null, MovementOrder::getSourceWarehouseId, dto.getSourceWarehouseId());
        lqw.eq(dto.getTargetWarehouseId() != null, MovementOrder::getTargetWarehouseId, dto.getTargetWarehouseId());
//        lqw.eq(dto.getOrderStatus() != null, MovementOrder::getOrderStatus, dto.getOrderStatus());
        lqw.eq(dto.getTotalQuantity() != null, MovementOrder::getTotalQuantity, dto.getTotalQuantity());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 新增移库单
     */
    @Override
    @Transactional
    public void insertByBo(MovementOrderDto dto) {
        // 2.创建移库单
        MovementOrder movementOrder = MapstructUtils.convert(dto, MovementOrder.class);
        movementOrder.setMoveNo(commonService.getNo(InventoryTypeEnum.MOVEMENT.getCode()));
        movementOrder.setMoveStatus(InventoryStatusEnum.FINISH.getCode());
        movementOrderMapper.insert(movementOrder);
        dto.setId(movementOrder.getId());
        // 3.创建移库单明细
        List<MovementOrderDetail> addDetailList = MapstructUtils.convert(dto.getDetails(), MovementOrderDetail.class);
        addDetailList.forEach(it -> {
            it.setId(movementOrder.getId());
        });
        movementOrderDetailService.saveDetails(addDetailList);
    }

    /**
     * 修改移库单
     */
    @Override
    @Transactional
    public void updateByBo(MovementOrderDto dto) {
        // 1.更新移库单
        MovementOrder update = MapstructUtils.convert(dto, MovementOrder.class);
        movementOrderMapper.updateById(update);
        // 2.保存移库单明细
        List<MovementOrderDetail> detailList = MapstructUtils.convert(dto.getDetails(), MovementOrderDetail.class);
        detailList.forEach(it -> it.setId(dto.getId()));
        movementOrderDetailService.saveDetails(detailList);
    }

    /**
     * 删除移库单
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        movementOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        MovementOrderVo movementOrderVo = queryById(id);
        if (movementOrderVo == null) {
            throw new com.divine.common.core.exception.base.BusinessException("移库单不存在");
        }
        if (InventoryStatusEnum.FINISH.getCode().equals(movementOrderVo.getMoveStatus())) {
            throw new BusinessException("移库单【" + movementOrderVo.getMoveNo() + "】已移库，无法删除！");
        }
    }

    /**
     * 批量删除移库单
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        movementOrderMapper.deleteBatchIds(ids);
    }

    /**
     * 移库
     *
     * @param dto
     */
    @Override
    @Transactional
    public void move(MovementOrderDto dto) {

        // 3.保存移库单核移库单明细
        if (Objects.isNull(dto.getId())) {
            insertByBo(dto);
        } else {
            updateByBo(dto);
        }
        // 4.更新库存Inventory
        MovementOrderDto shipmentBo = getShipmentBo(dto);
        inventoryService.subtract(shipmentBo.getDetails());
        MovementOrderDto receiptDto = getReceiptDto(dto);
        inventoryService.add(receiptDto.getDetails());
        // 6.创建库存记录流水
        inventoryHistoryService.saveInventoryHistory(shipmentBo, InventoryTypeEnum.MOVEMENT.getType(), false);
    }

    private MovementOrderDto getReceiptDto(MovementOrderDto dto) {

        MovementOrderDto receiptBo = SerializationUtils.clone(dto);
        receiptBo.getDetails().forEach(detail -> detail.setWarehouseId(detail.getTargetWarehouseId()));
        return receiptBo;
    }

    private MovementOrderDto getShipmentBo(MovementOrderDto dto) {
        MovementOrderDto shipmentBo = SerializationUtils.clone(dto);
        shipmentBo.getDetails().forEach(detail -> detail.setWarehouseId(detail.getSourceWarehouseId()));
        return shipmentBo;
    }

    private void validateBeforeMove(MovementOrderDto dto) {
        if (CollUtil.isEmpty(dto.getDetails())) {
            throw new com.divine.common.core.exception.base.BusinessException("物品明细不能为空！");
        }
    }
}
