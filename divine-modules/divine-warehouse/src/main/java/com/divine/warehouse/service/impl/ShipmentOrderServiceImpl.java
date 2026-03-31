package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.DateUtils;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.excel.utils.ExcelUtil;
import com.divine.system.service.CommonService;
import com.divine.warehouse.domain.dto.ItemDto;
import com.divine.warehouse.domain.dto.ShipmentOrderDetailDto;
import com.divine.warehouse.domain.dto.ShipmentOrderDto;
import com.divine.warehouse.domain.entity.*;
import com.divine.warehouse.domain.vo.*;
import com.divine.warehouse.mapper.ItemSkuMapper;
import com.divine.warehouse.mapper.ShipmentOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    private final GenerateNoUtil generateNoUtil;
    private final WarehouseMapper warehouseMapper;
    private final ItemService itemService;
    private final ItemSkuMapper skuMapper;

    /**
     * 查询出库单
     */
    @Override
    public ShipmentOrderVo queryById(Long id) {
        ShipmentOrderVo shipmentOrderVo = shipmentOrderMapper.selectVoById(id);
        if (shipmentOrderVo == null) {
            throw new BusinessException("出库单不存在");
        }
        // 获取仓库名称
        Warehouse warehouse = warehouseMapper.selectById(shipmentOrderVo.getWarehouseId());
        shipmentOrderVo.setWarehouseName(warehouse.getWarehouseName());
        shipmentOrderVo.setDetails(shipmentOrderDetailService.queryByShipmentOrderId(shipmentOrderVo.getId()));
        return shipmentOrderVo;
    }

    /**
     * 查询出库单列表
     */
    @Override
    public PageInfoRes<ShipmentOrderVo> queryPageList(ShipmentOrderDto dto, BasePage basePage) {
        // 查询物品信息
//        String item = dto.getItem();
//        String sku = dto.getSku();
//        if (StringUtils.isNotBlank(item) || StringUtils.isNotBlank(sku)) {
//            List<Long> shipmentIds = buildItemWrapper(item, sku);
//            if (CollUtil.isEmpty(shipmentIds)) return PageInfoRes.build();
//            dto.setShipmentIdList(shipmentIds);
//        }
        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(dto);
        Page<ShipmentOrderVo> result = shipmentOrderMapper.selectVoPage(basePage.build(), lqw);
        // 获取仓库信息
        List<ShipmentOrderVo> records = result.getRecords();
        List<Long> wareIds = records.stream().map(ShipmentOrderVo::getWarehouseId).toList();
        if (CollectionUtil.isNotEmpty(wareIds)) {
            List<Warehouse> warehouses = warehouseMapper.selectList(new LambdaQueryWrapper<>(Warehouse.class)
                .in(Warehouse::getId, wareIds));
            Map<Long, String> warehousesMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));
            records.forEach(r -> r.setWarehouseName(warehousesMap.get(r.getWarehouseId())));
        }
        return PageInfoRes.build(result);
    }


    /**
     * 构建物品信息查询条件
     *
     * @param item
     * @param sku
     */
    private List<Long> buildItemWrapper(String item, String sku) {
        List<Long> skuIds = new ArrayList<>();
        if (StringUtils.isNotBlank(item)) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemName(item);
            itemDto.setSkuNoOrName(sku);
            List<ItemInfoVo> itemInfoVos = itemService.queryItemInfo(itemDto);
            // 查询规格信息
            skuIds = itemInfoVos.stream()
                .map(ItemInfoVo::getSkuList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(List::stream)
                .map(ItemSkuVo::getId)
                .toList();

        } else {
            List<ItemSku> itemSkus = skuMapper.selectList(new LambdaQueryWrapper<>(ItemSku.class)
                .and(w -> w.like(ItemSku::getSkuNo, sku).or().like(ItemSku::getSkuName, sku)));
            skuIds = itemSkus.stream().map(ItemSku::getId).toList();
        }
        if (CollUtil.isEmpty(skuIds)) {
            return List.of();
        }
        ShipmentOrderDetailDto detailDto = new ShipmentOrderDetailDto();
        detailDto.setSkuIdList(skuIds);
        List<ShipmentOrderDetailVO> shipmentDetails = shipmentOrderDetailService.queryList(detailDto);
        return shipmentDetails.stream().map(ShipmentOrderDetailVO::getShipmentId).distinct().toList();
    }


    /**
     * 查询出库单列表
     */
    @Override
    public void export(ShipmentOrderDto dto, HttpServletResponse response) {
        // 如果数据量太大 考虑分批查
//        LambdaQueryWrapper<ShipmentOrder> lqw = buildQueryWrapper(dto);
        List<ShipmentImportVO> list = shipmentOrderMapper.selectShipmentList(dto);
        // 计算总价
        list.forEach(s -> s.setTotalPrice(s.getUnitPrice().multiply(new BigDecimal(s.getQuantity()))));

        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        String fileName = "出库记录";
        if (ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime)) {
            int sMonth = DateUtil.month(DateUtils.parseDate(startTime)) + 1;
            int eMonth = DateUtil.month(DateUtils.parseDate(endTime)) + 1;
            int sYear = DateUtil.year(DateUtils.parseDate(startTime));
            if (eMonth != sMonth) {
                fileName = "出库记录" + sYear + "年" + sMonth + "月" + eMonth + "月";
            } else {
                fileName = "出库记录" + sYear + "年" + sMonth + "月";
            }

        }
        ExcelUtil.exportExcel(list, fileName, ShipmentImportVO.class, response);
    }

    private LambdaQueryWrapper<ShipmentOrder> buildQueryWrapper(ShipmentOrderDto dto) {
        LambdaQueryWrapper<ShipmentOrder> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(dto.getShipmentNo()), ShipmentOrder::getShipmentNo, dto.getShipmentNo());
        lqw.in(CollUtil.isNotEmpty(dto.getShipmentIdList()), ShipmentOrder::getId, dto.getShipmentIdList());
        lqw.eq(dto.getOptType() != null, ShipmentOrder::getOptType, dto.getOptType());
        lqw.like(dto.getRecipient() != null, ShipmentOrder::getRecipient, dto.getRecipient());
        lqw.eq(dto.getTotalPrice() != null, ShipmentOrder::getTotalPrice, dto.getTotalPrice());
        lqw.eq(dto.getShipmentStatus() != null, ShipmentOrder::getShipmentStatus, dto.getShipmentStatus());
        lqw.eq(dto.getTotalQuantity() != null, ShipmentOrder::getTotalQuantity, dto.getTotalQuantity());
        lqw.ge(StringUtils.isNotBlank(dto.getStartTime()), ShipmentOrder::getCreateTime, dto.getStartTime());
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), ShipmentOrder::getCreateTime, dto.getEndTime());
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
        String shipmentNo = generateNoUtil.getBizNo(NoTypeEnum.SHIPMENT_NO.getCode());
        dto.setBizNo(shipmentNo);
        //组装数据保存
        ShipmentOrder shipmentOrder = MapstructUtils.convert(dto, ShipmentOrder.class);
        shipmentOrder.setShipmentNo(shipmentNo);
        shipmentOrderMapper.insert(shipmentOrder);
        dto.setId(shipmentOrder.getId());
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
            dto.setShipmentStatus(InventoryStatusEnum.FINISH.getCode());
            insertByBo(dto);
        } else {
            dto.setBizNo(dto.getShipmentNo());
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
