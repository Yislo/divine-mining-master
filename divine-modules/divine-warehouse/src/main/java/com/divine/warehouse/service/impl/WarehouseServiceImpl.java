package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.WarehouseDto;
import com.divine.warehouse.domain.entity.Warehouse;
import com.divine.warehouse.domain.vo.WarehouseVo;
import com.divine.warehouse.mapper.WarehouseMapper;
import com.divine.warehouse.service.InventoryService;
import com.divine.warehouse.service.WarehouseService;
import com.divine.common.core.constant.HttpStatus;
import com.divine.common.core.exception.ServiceException;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 仓库Service业务层处理
 *
 * @author zcc
 * @date 2024-07-16
 */
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final InventoryService inventoryService;

    /**
     * 查询仓库
     */
    @Override
    public WarehouseVo queryById(Long id) {
        return warehouseMapper.selectVoById(id);
    }

    /**
     * 查询仓库列表
     */
    @Override
    public PageInfoRes<WarehouseVo> queryPageList(WarehouseDto bo, BasePage basePage) {
        LambdaQueryWrapper<Warehouse> lqw = buildQueryWrapper(bo);
        Page<WarehouseVo> result = warehouseMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询仓库列表
     */
    @Override
    public List<WarehouseVo> queryList(WarehouseDto bo) {
        LambdaQueryWrapper<Warehouse> lqw = buildQueryWrapper(bo);
        return warehouseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Warehouse> buildQueryWrapper(WarehouseDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Warehouse> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getWarehouseCode()), Warehouse::getWarehouseCode, bo.getWarehouseCode());
        lqw.like(StrUtil.isNotBlank(bo.getWarehouseName()), Warehouse::getWarehouseName, bo.getWarehouseName());
        lqw.orderByAsc(Warehouse::getOrderNum);
        return lqw;
    }

    /**
     * 新增仓库
     */
    @Override
    public void insertByBo(WarehouseDto bo) {
        validateWarehouseNameAndNo(bo);
        Warehouse add = MapstructUtils.convert(bo, Warehouse.class);
        add.setOrderNum(this.getNextOrderNum());
        warehouseMapper.insert(add);
    }

    private Long getNextOrderNum() {
        LambdaQueryWrapper<Warehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Warehouse::getOrderNum);
        wrapper.last("limit 1");
        Warehouse warehouse = warehouseMapper.selectOne(wrapper);
        return warehouse == null ? 0L : warehouse.getOrderNum() + 1;
    }

    /**
     * 修改仓库
     */
    @Override
    public void updateByBo(WarehouseDto bo) {
        validateWarehouseNameAndNo(bo);
        Warehouse update = MapstructUtils.convert(bo, Warehouse.class);
        warehouseMapper.updateById(update);
    }

    private void validateWarehouseNameAndNo(WarehouseDto warehouse) {
        LambdaQueryWrapper<Warehouse> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Warehouse::getWarehouseName, warehouse.getWarehouseName()).or().eq(StrUtil.isNotBlank(warehouse.getWarehouseCode()), Warehouse::getWarehouseCode, warehouse.getWarehouseCode());
        List<Warehouse> warehouseList = warehouseMapper.selectList(queryWrapper);
        boolean validateNameResult = warehouseList.stream().anyMatch(
            it -> Objects.equals(it.getWarehouseName(), warehouse.getWarehouseName()) && !Objects.equals(it.getId(), warehouse.getId()));
        Assert.isFalse(validateNameResult, "仓库名称重复");
        boolean validateNoResult = warehouseList.stream().anyMatch(
            it -> Objects.equals(it.getWarehouseCode(), warehouse.getWarehouseCode()) && !Objects.equals(it.getId(), warehouse.getId()));
        Assert.isFalse(validateNoResult, "仓库编号重复");
    }

    /**
     * 删除仓库
     */
    @Override
    public void deleteById(Long id) {
        validIdBeforeDelete(id);
        warehouseMapper.deleteById(id);
    }

    private void validIdBeforeDelete(Long id) {
        if (inventoryService.existsByWarehouseId(id)) {
            throw new ServiceException("删除失败", HttpStatus.CONFLICT, "该仓库已有业务关联，无法删除！");
        }
    }

    /**
     * 批量删除仓库
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        warehouseMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateOrderNum(List<WarehouseDto> tree) {
        if (CollUtil.isEmpty(tree)) {
            return;
        }
        List<Warehouse> updateList = MapstructUtils.convert(tree, Warehouse.class);
        for (int i = 0; i < updateList.size(); i++) {
            updateList.get(i).setOrderNum((long) i);
        }
        saveOrUpdateBatch(updateList);
    }
}
