package com.divine.warehouse.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.warehouse.domain.dto.MovementOrderDetailDto;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.vo.MovementOrderDetailVO;
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
 * @author yisl
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
    public MovementOrderDetailVO queryById(Long id){
        return movementOrderDetailMapper.selectVoById(id);
    }

    /**
     * 查询库存移动详情列表
     */
    @Override
    public PageInfoRes<MovementOrderDetailVO> queryPageList(MovementOrderDetailDto dto, BasePage basePage) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(dto);
        Page<MovementOrderDetailVO> result = movementOrderDetailMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询库存移动详情列表
     */
    @Override
    public List<MovementOrderDetailVO> queryList(MovementOrderDetailDto dto) {
        LambdaQueryWrapper<MovementOrderDetail> lqw = buildQueryWrapper(dto);
        return movementOrderDetailMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MovementOrderDetail> buildQueryWrapper(MovementOrderDetailDto dto) {
        Map<String, Object> params = dto.getParams();
        LambdaQueryWrapper<MovementOrderDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getMoveId() != null, MovementOrderDetail::getMoveId, dto.getMoveId());
        lqw.eq(dto.getSkuId() != null, MovementOrderDetail::getSkuId, dto.getSkuId());
        lqw.eq(dto.getQuantity() != null, MovementOrderDetail::getQuantity, dto.getQuantity());
        lqw.eq(dto.getSourceWarehouseId() != null, MovementOrderDetail::getSourceWarehouseId, dto.getSourceWarehouseId());
        lqw.eq(dto.getTargetWarehouseId() != null, MovementOrderDetail::getTargetWarehouseId, dto.getTargetWarehouseId());
        return lqw;
    }

    /**
     * 新增库存移动详情
     */
    @Override
    public void insertByBo(MovementOrderDetailDto dto) {
        MovementOrderDetail add = MapstructUtils.convert(dto, MovementOrderDetail.class);
        movementOrderDetailMapper.insert(add);
    }

    /**
     * 修改库存移动详情
     */
    @Override
    public void updateByBo(MovementOrderDetailDto dto) {
        MovementOrderDetail update = MapstructUtils.convert(dto, MovementOrderDetail.class);
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
    public List<MovementOrderDetailVO> queryByMovementOrderId(Long movementOrderId) {
        MovementOrderDetailDto dto = new MovementOrderDetailDto();
        dto.setMoveId(movementOrderId);
        List<MovementOrderDetailVO> details = queryList(dto);
        if (CollUtil.isEmpty(details)) {
            return Collections.emptyList();
        }
        itemSkuService.setItemSkuMap(details);
        return details;
    }
}
