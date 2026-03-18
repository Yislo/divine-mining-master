package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.FileBizTypeEnum;
import com.divine.common.core.enums.InventoryStatusEnum;
import com.divine.common.core.enums.InventoryTypeEnum;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.system.domain.dto.SysFileDTO;
import com.divine.system.service.SysFileService;
import com.divine.warehouse.domain.dto.BaseOrderDetailDto;
import com.divine.warehouse.domain.dto.ReceiptImportDTO;
import com.divine.warehouse.domain.dto.ReceiptOrderDetailDto;
import com.divine.warehouse.domain.dto.ReceiptOrderDto;
import com.divine.warehouse.domain.entity.*;
import com.divine.warehouse.domain.vo.BaseOrderDetailVO;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.warehouse.mapper.ItemMapper;
import com.divine.warehouse.mapper.ItemSkuMapper;
import com.divine.warehouse.mapper.ReceiptOrderMapper;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.*;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.domain.BaseEntity;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.google.api.client.util.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
    private final GenerateNoUtil generateNoUtil;
    private final WarehouseMapper warehouseMapper;
    private final SysFileService sysFileService;
    private final MerchantService merchantService;
    private final ItemSkuMapper itemSkuMapper;
    private final ItemMapper itemMapper;

    /**
     * 查询入库单
     */
    @Override
    public ReceiptOrderVo queryById(Long id) {
        ReceiptOrderVo receiptOrderVo = receiptOrderMapper.selectVoById(id);
        if (ObjUtil.isNull(receiptOrderVo)) {
            throw new BusinessException("入库单不存在");
        }
        // 获取仓库名称
        Warehouse warehouse = warehouseMapper.selectById(receiptOrderVo.getWarehouseId());
        receiptOrderVo.setWarehouseName(warehouse.getWarehouseName());
        // 获取供应商名称
        MerchantVo merchantVo = merchantService.queryById(receiptOrderVo.getMerchantId());
        if (ObjUtil.isNotNull(merchantVo)) {
            receiptOrderVo.setMerchantName(merchantVo.getMerchantName());
        }
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
        lqw.like(StringUtils.isNotBlank(dto.getReceiptNo()), ReceiptOrder::getReceiptNo, dto.getReceiptNo());
        lqw.eq(dto.getOptType() != null, ReceiptOrder::getOptType, dto.getOptType());
        lqw.eq(dto.getMerchantId() != null, ReceiptOrder::getMerchantId, dto.getMerchantId());
        lqw.eq(dto.getReceiptStatus() != null, ReceiptOrder::getReceiptStatus, dto.getReceiptStatus());
        lqw.like(dto.getBizOrderNo() != null, ReceiptOrder::getBizOrderNo, dto.getBizOrderNo());
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
        String receiptNo = generateNoUtil.getBizNo(NoTypeEnum.RECEIPT_NO.getCode());
        dto.setBizNo(receiptNo);
        // 考虑是否计算总金额 是否完全信任前端
        ReceiptOrder receiptOrder = MapstructUtils.convert(dto, ReceiptOrder.class);
        receiptOrder.setReceiptNo(receiptNo);
        receiptOrderMapper.insert(receiptOrder);
        dto.setId(receiptOrder.getId());
        List<ReceiptOrderDetailDto> detailDtoList = dto.getDetails();
        detailDtoList.forEach(it -> it.setReceiptId(receiptOrder.getId()));
        // 创建入库单明细
        receiptOrderDetailService.saveDetails(detailDtoList);
        // 保存文件
        List<SysFileDTO> allFiles = new ArrayList<>();
        List<ItemSku> sku = Lists.newArrayList();
        // 组装文件数据
        detailDtoList.forEach(d -> {
            // 更新sku价格
            ItemSku oldSku = itemSkuMapper.selectById(d.getSkuId());
            if (oldSku.getUnitPrice() == null || oldSku.getUnitPrice().compareTo(d.getUnitPrice()) == 0) {
                ItemSku itemSku = new ItemSku();
                itemSku.setId(d.getSkuId());
                itemSku.setUnitPrice(d.getUnitPrice());
                sku.add(itemSku);
            }
            // 保存文件信息
            List<String> fileList = d.getFileList();
            if (CollectionUtil.isNotEmpty(fileList)) {
                List<SysFileDTO> files = fileList.stream()
                    .map(f -> {
                        SysFileDTO file = new SysFileDTO();
                        file.setBizId(d.getId());
                        file.setBizType(FileBizTypeEnum.RECEIPT_DETAIL.getCode());
                        file.setFileName(SysFileDTO.getFileName(f));
                        file.setFileUrl(f);
                        return file;
                    }).toList();
                allFiles.addAll(files);
            }
        });
        if (CollectionUtil.isNotEmpty(allFiles)) {
            sysFileService.batchSaveFile(allFiles);
        }
        // 更新sku价格
        itemSkuMapper.updateBatchById(sku);
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
            dto.setBizNo(dto.getReceiptNo());
            updateByBo(dto);
            // 删除历史文件
            List<Long> list = dto.getDetails().stream().map(BaseOrderDetailDto::getId).toList();
            sysFileService.deleteFileByIds(list, false);
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
        List<ReceiptOrderDetailDto> detailList = dto.getDetails();
        //需要考虑detail删除
        List<ReceiptOrderDetailVO> dbList = receiptOrderDetailService.queryByReceiptOrderId(dto.getId());
        Set<Long> ids = detailList.stream()
            .map(ReceiptOrderDetailDto::getId)
            .filter(Objects::nonNull).collect(Collectors.toSet());
        List<ReceiptOrderDetailVO> delList = dbList.stream().filter(it -> !ids.contains(it.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(delList)) {
            receiptOrderDetailService.deleteByIds(delList.stream().map(BaseOrderDetailVO::getId).collect(Collectors.toList()));
        }
        detailList.forEach(it -> it.setReceiptId(dto.getId()));
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
        if (ObjUtil.isNull(receiptOrderVo)) {
            throw new BusinessException("入库单不存在");
        }
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

    /**
     * 批量导入入库单
     *
     * @param dtoList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBatch(List<ReceiptImportDTO> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            return;
        }
        // 保存入库单
        ReceiptOrder receiptOrder = saveReceiptOrder(dtoList);
        Long receiptId = receiptOrder.getId();
        // 新增物品信息
        saveItemInfo(dtoList);
        // 创建入库单明细
        List<ReceiptOrderDetailDto> list = dtoList.stream().map(dto -> {
            ReceiptOrderDetailDto receiptOrderDetail = new ReceiptOrderDetailDto();
            receiptOrderDetail.setReceiptId(receiptId);
            receiptOrderDetail.setSkuId(dto.getSkuId());
            receiptOrderDetail.setQuantity(dto.getQuantity());
            receiptOrderDetail.setUnitPrice(dto.getUnitPrice());
            receiptOrderDetail.setWarehouseId(dto.getWarehouseId());
            receiptOrderDetail.setStorageShelf(dto.getStorageShelf());
            receiptOrderDetail.setRemark(dto.getRemark());
            return receiptOrderDetail;
        }).toList();
        // 创建入库单明细
        receiptOrderDetailService.saveDetails(list);
        // 3.增加库存
        inventoryService.add(list);
        // 4.保存库存记录
        ReceiptOrderDto dto = new ReceiptOrderDto();
        dto.setBizNo(receiptOrder.getReceiptNo());
        dto.setId(receiptId);
        dto.setDetails(list);
        inventoryHistoryService.saveInventoryHistory(dto, InventoryTypeEnum.RECEIPT.getType(), true);

    }

    /**
     * 保存入库单
     *
     * @param dtoList
     */
    private ReceiptOrder saveReceiptOrder(List<ReceiptImportDTO> dtoList) {
        // 组装入库单数据
        ReceiptOrder receiptOrder = new ReceiptOrder();
        // 计算总数
        Long totalQuantity = dtoList.stream()
            .map(ReceiptImportDTO::getQuantity)
            .filter(Objects::nonNull)
            .reduce(0L, Long::sum);
        // 计算总金额
        BigDecimal totalPrice = dtoList.stream()
            .filter(item -> item.getQuantity() != null && item.getUnitPrice() != null)
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        receiptOrder.setTotalPrice(totalPrice);
        receiptOrder.setTotalQuantity(totalQuantity);
        receiptOrder.setReceiptNo(generateNoUtil.getBizNo(NoTypeEnum.RECEIPT_NO.getCode()));
        receiptOrder.setReceiptStatus(InventoryStatusEnum.FINISH.getCode());
        receiptOrder.setOptType(0);
        receiptOrder.setWarehouseId(dtoList.get(0).getWarehouseId());
        receiptOrderMapper.insert(receiptOrder);
        return receiptOrder;
    }

    /**
     * 保存物品信息
     *
     * @param dtoList
     */
    private void saveItemInfo(List<ReceiptImportDTO> dtoList) {

        // 1 查询数据库已有物品（去重）
        List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<Item>()
            .in(Item::getItemName, dtoList.stream()
                .map(ReceiptImportDTO::getItemName).distinct()
                .toList()));
        // 2 转换为 Map：itemName -> Item
        Map<String, Item> itemMap = items.stream()
            .collect(Collectors.toMap(Item::getItemName, Function.identity()));
        // 3 找出不存在的物品
        List<Item> newItems = dtoList.stream()
            .collect(Collectors.toMap(ReceiptImportDTO::getItemName, dto -> dto, (a, b) -> a))
            .values()
            .stream()
            // 2. 过滤掉 itemMap 中已存在的（数据库已有的）
            .filter(dto -> !itemMap.containsKey(dto.getItemName()))
            // 3. 构造新对象
            .map(dto -> {
                Item item = new Item();
                item.setItemName(dto.getItemName());
                item.setItemNo(generateNoUtil.getBizNo(NoTypeEnum.SPU_NO.getCode()));
                item.setUnit(dto.getUnit());
                return item;
            })
            .toList();
        // 4 批量新增 Item
        if (!newItems.isEmpty()) {
            itemMapper.insertBatch(newItems);
            newItems.forEach(item -> itemMap.put(item.getItemName(), item));
        }
        // 5 回填 itemId
        dtoList.forEach(dto -> {
            Item item = itemMap.get(dto.getItemName());
            if (item != null) {
                dto.setItemId(item.getId());
            }
        });
        // ===============================
        // 处理 SKU（规格）
        // ===============================

        // 6 获取 itemId + skuName 组合
        Set<String> skuKeys = dtoList.stream()
            .map(dto -> dto.getItemId() + "_" + dto.getSkuName())
            .collect(Collectors.toSet());
        // 7 查询已有 SKU
        List<ItemSku> skus = itemSkuMapper.selectList(
            new LambdaQueryWrapper<ItemSku>()
                .in(ItemSku::getItemId, dtoList.stream().map(ReceiptImportDTO::getItemId).collect(Collectors.toSet()))
                .in(ItemSku::getSkuName, dtoList.stream().map(ReceiptImportDTO::getSkuName).distinct().toList())
        );
        // 8 转换为 Map：itemId_skuName -> SKU
        Map<String, ItemSku> skuMap = skus.stream()
            .collect(Collectors.toMap(
                sku -> sku.getItemId() + "_" + sku.getSkuName(),
                Function.identity(),
                (a, b) -> a
            ));
        // 9. 构造“覆盖式”更新/新增列表
        Map<String, ItemSku> skuToSaveMap = new HashMap<>();
        for (ReceiptImportDTO dto : dtoList) {
            String key = dto.getItemId() + "_" + dto.getSkuName();

            // 如果已经在本次循环处理过了，跳过（或者根据业务逻辑取最后一条/最新单价）
            if (skuToSaveMap.containsKey(key)) continue;

            ItemSku sku = new ItemSku();
            // 如果数据库里已有该 SKU，把 ID 拿出来赋值给对象，触发 MyBatis-Plus 的 Update 逻辑
            if (skuMap.containsKey(key)) {
                sku.setId(skuMap.get(key).getId());
            } else {
                // 如果数据库没有，则是新增，生成编号
                sku.setSkuNo(generateNoUtil.getBizNo(NoTypeEnum.SKU_NO.getCode()));
            }
            // 覆盖历史单价
            sku.setUnitPrice(dto.getUnitPrice());
            sku.setItemId(dto.getItemId());
            sku.setSkuName(dto.getSkuName());
            skuToSaveMap.put(key, sku);
        }
        // 10. 执行批量操作并回填 ID
        if (!skuToSaveMap.isEmpty()) {
            List<ItemSku> saveList = new ArrayList<>(skuToSaveMap.values());
            itemSkuMapper.insertOrUpdateBatch(saveList);
            // 将最新的对象（包含已回填的 ID）同步更新到 skuMap，供下一步回填给 DTO
            saveList.forEach(sku -> skuMap.put(sku.getItemId() + "_" + sku.getSkuName(), sku));
        }

        // 11. 统一回填最新的 skuId 到 dtoList
        dtoList.forEach(dto -> {
            String key = dto.getItemId() + "_" + dto.getSkuName();
            ItemSku sku = skuMap.get(key);
            if (sku != null) {
                dto.setSkuId(sku.getId());
            }
        });
    }
}
