package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.MovementOrderDetailDto;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.vo.MovementOrderDetailVo;
import com.divine.warehouse.mapper.MovementOrderDetailMapper;
import com.divine.warehouse.service.ItemSkuService;
import com.divine.warehouse.service.MovementOrderDetailService;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 库存移动详情Service业务层处理
 *
 * @author zcc
 * @date 2024-08-09
 */
@RequiredArgsConstructor
@Service
public class MovementOrderDetailServiceImpl extends ServiceImpl<MovementOrderDetailMapper, MovementOrderDetail> implements MovementOrderDetailService {

    private final MovementOrderDetailMapper movementOrderDetailMapper;
    private final ItemSkuService itemSkuService;

    /**
     * 查询库存移动详情
     */
    @Override
    public MovementOrderDetailVo queryById(Long id){
        return movementOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询库存移动详情列表
     */
    @Override
    public PageInfoRes<MovementOrderDetailVo> queryPageList(MovementOrderDetailDto bo, BasePage basePage) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(bo);
        Page<MovementOrderDetailVo> result = movementOrderDetailMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存移动详情列表
     */
    @Override
    public List<MovementOrderDetailVo> queryList(MovementOrderDetailDto bo) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(bo);
        return movementOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MovementOrderDetail> buildQueryWrapper(MovementOrderDetailDto bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<MovementOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getOrderId() != null, MovementOrderDetail::getOrderId, bo.getOrderId());
        lqw.eq(bo.getSkuId() != null, MovementOrderDetail::getSkuId, bo.getSkuId());
        lqw.eq(bo.getQuantity() != null, MovementOrderDetail::getQuantity, bo.getQuantity());
        lqw.eq(bo.getSourceWarehouseId() != null, MovementOrderDetail::getSourceWarehouseId, bo.getSourceWarehouseId());
        lqw.eq(bo.getTargetWarehouseId() != null, MovementOrderDetail::getTargetWarehouseId, bo.getTargetWarehouseId());
        return lqw;
    }

    /**
     * 新增库存移动详情
     */
    @Override
    public void insertByBo(MovementOrderDetailDto bo) {
        MovementOrderDetail add = MapstructUtils.convert(bo, MovementOrderDetail.class);
        movementOrderDetailMapper.insert(add);
    }

    /**
     * 修改库存移动详情
     */
    @Override
    public void updateByBo(MovementOrderDetailDto bo) {
        MovementOrderDetail update = MapstructUtils.convert(bo, MovementOrderDetail.class);
        movementOrderDetailMapper.updateById(update);
    }

    /**
     * 批量删除库存移动详情
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        movementOrderDetailMapper.deleteBatchIds(ids);
    }

    @Override
    @Transactional
    public void saveDetails(List<MovementOrderDetail> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        saveOrUpdateBatch(list);
    }

    /**
     * 根据移库单id查询移库单详情
     * @param movementOrderId
     * @return
     */
    @Override
    public List<MovementOrderDetailVo> queryByMovementOrderId(Long movementOrderId) {
        MovementOrderDetailDto bo = new MovementOrderDetailDto();
        bo.setOrderId(movementOrderId);
        List<MovementOrderDetailVo> details = queryList(bo);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
