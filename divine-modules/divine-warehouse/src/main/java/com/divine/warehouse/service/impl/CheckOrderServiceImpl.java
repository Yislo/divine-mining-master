package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.warehouse.domain.dto.CheckOrderDetailDto;
import com.divine.warehouse.domain.dto.CheckOrderDto;
import com.divine.warehouse.domain.entity.CheckOrder;
import com.divine.warehouse.domain.entity.CheckOrderDetail;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.CheckOrderVo;
import com.divine.warehouse.domain.vo.ShipmentOrderVo;
import com.divine.warehouse.mapper.CheckOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 库存盘点单据Service业务层处理
 *
 * @author yisl
 * @date 2024-08-13
 */
@RequiredArgsConstructor
@Service
public class CheckOrderServiceImpl implements CheckOrderService {

    private final CheckOrderMapper checkOrderMapper;
    private final CheckOrderDetailService checkOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryHistoryService inventoryHistoryService;
    private final CommonService commonService;
    private final WarehouseMapper warehouseMapper;

    /**
     * 查询库存盘点单据
     */
    @Override
    public CheckOrderVo queryById(Long id) {
        CheckOrderVo checkOrderVo = checkOrderMapper.selectVoById(id);
        if (checkOrderVo == null) {
            throw new com.divine.common.core.exception.base.BusinessException("盘库单不存在");
        }
        checkOrderVo.setDetails(checkOrderDetailService.queryByCheckOrderId(id));
        return checkOrderVo;
    }

    /**
     * 查询库存盘点单据列表
     */
    @Override
    public PageInfoRes<CheckOrderVo> queryPageList(CheckOrderDto dto, BasePage basePage) {
        LambdaQueryWrapper<CheckOrder> lqw = buildQueryWrapper(dto);
        Page<CheckOrderVo> result = checkOrderMapper.selectVoPage(basePage.build(), lqw);
        // 获取仓库信息
        List<CheckOrderVo> records = result.getRecords();
        List<Long> wareIds = records.stream().map(CheckOrderVo::getWarehouseId).toList();
        if (CollectionUtil.isNotEmpty(wareIds)){
            List<Warehouse> warehouses = warehouseMapper.selectList(new LambdaQueryWrapper<>(Warehouse.class)
                .in(Warehouse::getId, wareIds));
            Map<Long, String> warehousesMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));
            records.forEach(r-> r.setWarehouseName(warehousesMap.get(r.getWarehouseId())));
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存盘点单据列表
     */
    @Override
    public List<CheckOrderVo> queryList(CheckOrderDto dto) {
        LambdaQueryWrapper<CheckOrder> lqw = buildQueryWrapper(dto);
        return checkOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<CheckOrder> buildQueryWrapper(CheckOrderDto dto) {
        LambdaQueryWrapper<CheckOrder> lqw = Wrappers.lambdaQuery();
//        lqw.eq(StringUtils.isNotBlank(dto.getOrderNo()), CheckOrder::getOrderNo, dto.getOrderNo());
//        lqw.eq(dto.getOrderStatus() != null, CheckOrder::getOrderStatus, dto.getOrderStatus());
        lqw.eq(dto.getTotalQuantity() != null, CheckOrder::getTotalQuantity, dto.getTotalQuantity());
        lqw.eq(dto.getWarehouseId() != null, CheckOrder::getWarehouseId, dto.getWarehouseId());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 新增库存盘点单据
     */
    @Override
    @Transactional
    public void insertByBo(CheckOrderDto dto) {
        // 创建盘库单
        String checkNo = commonService.getNo(InventoryTypeEnum.CHECK.getCode());
        dto.setBizNo(checkNo);
        CheckOrder checkOrder = MapstructUtils.convert(dto, CheckOrder.class);
        checkOrder.setCheckNo(checkNo);
        checkOrder.setCheckStatus(InventoryStatusEnum.FINISH.getCode());
        checkOrderMapper.insert(checkOrder);
        // 创建盘库单明细
        List<CheckOrderDetail> addDetailList = MapstructUtils.convert(dto.getDetails(), CheckOrderDetail.class);
        addDetailList.forEach(it -> it.setCheckId(checkOrder.getId()));
        checkOrderDetailService.saveDetails(addDetailList);
    }

    /**
     * 修改库存盘点单据
     */
    @Override
    @Transactional
    public void updateByBo(CheckOrderDto dto) {
        // 更新盘库单
        CheckOrder update = MapstructUtils.convert(dto, CheckOrder.class);
        checkOrderMapper.updateById(update);
        // 保存盘库单明细
        List<CheckOrderDetail> detailList = MapstructUtils.convert(dto.getDetails(), CheckOrderDetail.class);
        detailList.forEach(it -> it.setCheckId(dto.getId()));
        checkOrderDetailService.saveDetails(detailList);
    }

    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        checkOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        CheckOrderVo checkOrderVo = queryById(id);
        if (checkOrderVo == null) {
            throw new com.divine.common.core.exception.base.BusinessException("盘库单不存在");
        }
        if (InventoryStatusEnum.FINISH.getCode().equals(checkOrderVo.getCheckStatus())) {
            throw new BusinessException("盘库单【" + checkOrderVo.getCheckNo() + "】已盘库完成，无法删除！");
        }
    }

    /**
     * 批量删除库存盘点单据
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        checkOrderMapper.deleteBatchIds(ids);
    }

    /**
     * @param dto
     */
    @Override
    @Transactional
    public void check(CheckOrderDto dto) {
        List<CheckOrderDetailDto> details = dto.getDetails();
        // 保存盘库单 check order
        if (Objects.isNull(dto.getId())) {
            insertByBo(dto);
        } else {
            updateByBo(dto);
        }
        // 保存库存 inventory
        inventoryService.updateInventory(details);
        // 新增库存记录 inventory history
        CheckOrderDto filterBo = this.filterCheckOrderDetail(dto);
        inventoryHistoryService.saveInventoryHistory(filterBo, InventoryTypeEnum.CHECK.getType(), true);
    }

    private CheckOrderDto filterCheckOrderDetail(CheckOrderDto dto) {
        CheckOrderDto filterBo = SerializationUtils.clone(dto);
        List<CheckOrderDetailDto> details = filterBo.getDetails().stream().filter(detail -> {
            long result = detail.getCheckQuantity() - detail.getQuantity();
            return result == 0;
        }).peek(detail -> {
            long result = detail.getCheckQuantity() - detail.getQuantity();
            detail.setBeforeQuantity(detail.getQuantity());
            detail.setAfterQuantity(detail.getCheckQuantity());
            detail.setQuantity(result);
        }).toList();
        filterBo.setDetails(details);
        return filterBo;
    }
}
