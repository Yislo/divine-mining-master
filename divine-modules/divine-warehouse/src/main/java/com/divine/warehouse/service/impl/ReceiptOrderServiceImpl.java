package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.FileBizTypeEnum;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.system.domain.dto.SysFileDTO;
import com.divine.system.service.SysFileService;
import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.ReceiptOrderDetailDto;
import com.divine.warehouse.domain.dto.ReceiptOrderDto;
import com.divine.warehouse.domain.entity.BaseOrderDetail;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.BaseOrderDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.warehouse.mapper.ReceiptOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 入库单Service业务层处理
 *
 * @author yisl
 * @date 2024-07-19
 */
@RequiredArgsConstructor
@Service
public class ReceiptOrderServiceImpl implements ReceiptOrderService {

    private final ReceiptOrderMapper receiptOrderMapper;
    private final ReceiptOrderDetailService receiptOrderDetailService;
    private final InventoryService inventoryService;
    private final InventoryHistoryService inventoryHistoryService;
    private final CommonService commonService;
    private final WarehouseMapper warehouseMapper;
    private final SysFileService sysFileService;

    /**
     * 查询入库单
     */
    @Override
    public ReceiptOrderVo queryById(Long id) {
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        // 获取仓库名称
        Warehouse warehouse = warehouseMapper.selectById(receiptOrderVo.getWarehouseId());
        receiptOrderVo.setWarehouseName(warehouse.getWarehouseName());
        receiptOrderVo.setDetails(receiptOrderDetailService.queryByReceiptOrderId(id));
        return receiptOrderVo;
    }

    @Override
    public Long queryIdByOrderNo(String orderNo) {
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoOne(new QueryWrapper<ReceiptOrder>().eq("order_no", orderNo));
        return receiptOrderVo != null ? receiptOrderVo.getId() : null;
    }

    /**
     * 查询入库单列表
     */
    @Override
    public PageInfoRes<ReceiptOrderVo> queryPageList(ReceiptOrderDto dto, BasePage basePage) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(dto);
        Page<ReceiptOrderVo> result = receiptOrderMapper.selectVoPage(basePage.build(), lqw);
        // 获取仓库信息
        List<ReceiptOrderVo> records = result.getRecords();
        List<Long> wareIds = records.stream().map(ReceiptOrderVo::getWarehouseId).toList();
        if (CollectionUtil.isNotEmpty(wareIds)) {
            List<Warehouse> warehouses = warehouseMapper.selectList(new LambdaQueryWrapper<>(Warehouse.class)
                .in(Warehouse::getId, wareIds));
            Map<Long, String> warehousesMap = warehouses.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getWarehouseName));
            records.forEach(r -> r.setWarehouseName(warehousesMap.get(r.getWarehouseId())));
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询入库单列表
     */
    @Override
    public List<ReceiptOrderVo> queryList(ReceiptOrderDto dto) {
        LambdaQueryWrapper<ReceiptOrder> lqw = buildQueryWrapper(dto);
        return receiptOrderMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ReceiptOrder> buildQueryWrapper(ReceiptOrderDto dto) {
        LambdaQueryWrapper<ReceiptOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getBizNo()), ReceiptOrder::getReceiptNo, dto.getBizNo());
        lqw.eq(dto.getOptType() != null, ReceiptOrder::getOptType, dto.getOptType());
        lqw.eq(dto.getMerchantId() != null, ReceiptOrder::getMerchantId, dto.getMerchantId());
        lqw.eq(dto.getReceiptStatus() != null, ReceiptOrder::getReceiptStatus, dto.getReceiptStatus());
        lqw.eq(dto.getBizOrderNo() != null, ReceiptOrder::getBizOrderNo, dto.getBizOrderNo());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        return lqw;
    }

    /**
     * 暂存入库单
     */
    @Override
    @Transactional
    public Long insertByBo(ReceiptOrderDto dto) {
        dto.setReceiptStatus(0);
        List<Long> list = dto.getDetails().stream().map(BaseOrderDetailDto::getId).toList();
        sysFileService.deleteFileByIds(list, false);
        return insertReceipt(dto);
    }

    /**
     * 保存入库单
     *
     * @param dto
     * @return
     */
    private Long insertReceipt(ReceiptOrderDto dto) {
        // 创建入库单
        String receiptNo = commonService.getNo(InventoryTypeEnum.RECEIPT.getCode());
        dto.setBizNo(receiptNo);
        // 考虑是否计算总金额 是否完全信任前端
        ReceiptOrder receiptOrder = MapstructUtils.convert(dto, ReceiptOrder.class);
        receiptOrder.setReceiptNo(receiptNo);
        receiptOrderMapper.insert(receiptOrder);
        List<ReceiptOrderDetailDto> detailBoList = dto.getDetails();
        List<ReceiptOrderDetail> addDetailList = MapstructUtils.convert(detailBoList, ReceiptOrderDetail.class);
        addDetailList.forEach(it -> it.setReceiptId(receiptOrder.getId()));
        // 创建入库单明细
        receiptOrderDetailService.saveDetails(addDetailList);
        // 保存文件
        List<SysFileDTO> allFiles = new ArrayList<>();
        // 组装文件数据
        dto.getDetails().forEach(d -> {
            List<SysFileDTO> files = d.getFileList().stream()
                .map(f -> {
                    SysFileDTO file = new SysFileDTO();
                    file.setBizId(d.getId());
                    file.setBizType(FileBizTypeEnum.RECEIPT_DETAIL.getCode());
                    file.setFileName(SysFileDTO.getFileName(f));
                    file.setFileUrl(f);
                    return file;
                }).toList();
            allFiles.addAll(files);
        });
        sysFileService.batchSaveFile(allFiles);
        return receiptOrder.getId();
    }


    /**
     * 入库：
     * 1.校验
     * 2.保存入库单和入库单明细
     * 3.保存库存明细
     * 4.增加库存
     * 5.保存库存记录
     */
    @Override
    @Transactional
    public void warehousing(ReceiptOrderDto dto) {
        // 2. 保存入库单和入库单明细
        dto.setReceiptStatus(1);
        if (Objects.isNull(dto.getId())) {
            insertReceipt(dto);
        } else {
            updateByBo(dto);
        }
        // 3.增加库存
        inventoryService.add(dto.getDetails());
        // 4.保存库存记录
        inventoryHistoryService.saveInventoryHistory(dto, InventoryTypeEnum.RECEIPT.getType(), true);
    }

    private void validateBeforeReceive(ReceiptOrderDto dto) {
        if (CollUtil.isEmpty(dto.getDetails())) {
            throw new com.divine.common.core.exception.base.BusinessException("物品明细不能为空");
        }
    }

    /**
     * 修改入库单
     */
    @Override
    @Transactional
    public void updateByBo(ReceiptOrderDto dto) {
        // 更新入库单
        ReceiptOrder update = MapstructUtils.convert(dto, ReceiptOrder.class);
        receiptOrderMapper.updateById(update);
        // 保存入库单明细
        List<ReceiptOrderDetail> detailList = MapstructUtils.convert(dto.getDetails(), ReceiptOrderDetail.class);
        //需要考虑detail删除
        List<ReceiptOrderDetailVO> dbList = receiptOrderDetailService.queryByReceiptOrderId(dto.getId());
        Set<Long> ids = detailList.stream()
            .map(BaseOrderDetail::getId)
            .filter(Objects::nonNull).collect(Collectors.toSet());
        List<ReceiptOrderDetailVO> delList = dbList.stream().filter(it -> !ids.contains(it.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(delList)) {
            receiptOrderDetailService.deleteByIds(delList.stream().map(BaseOrderDetailVO::getId).collect(Collectors.toList()));
        }
        detailList.forEach(it -> it.setId(dto.getId()));
        receiptOrderDetailService.saveDetails(detailList);
    }

    /**
     * 入库单作废
     *
     * @param id
     */
    @Override
    public void editToInvalid(Long id) {
        LambdaUpdateWrapper<ReceiptOrder> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(ReceiptOrder::getId, id);
        wrapper.set(ReceiptOrder::getReceiptStatus, InventoryStatusEnum.INVALID.getCode());
        receiptOrderMapper.update(null, wrapper);
    }

    /**
     * 删除入库单
     */
    @Override
    public void deleteById(Long id) {
        validateIdBeforeDelete(id);
        receiptOrderMapper.deleteById(id);
    }

    private void validateIdBeforeDelete(Long id) {
        ReceiptOrderVo receiptOrderVo = queryById(id);
        Assert.notNull(receiptOrderVo, "入库单不存在");
        if (InventoryStatusEnum.FINISH.getCode().equals(receiptOrderVo.getReceiptStatus())) {
            throw new BusinessException("入库单【" + receiptOrderVo.getReceiptNo() + "】已入库，无法删除！");
        }
    }

    /**
     * 批量删除入库单
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        receiptOrderMapper.deleteBatchIds(ids);
    }


}
