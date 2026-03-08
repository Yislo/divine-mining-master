package com.divine.mine.service.impl;

import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineWeightingDto;
import com.divine.mine.domain.entity.MineWeighting;
import com.divine.mine.domain.vo.MineWeightingVo;
import com.divine.mine.service.MineWeightingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineWeightingMapper;

import java.util.List;
import java.util.Collection;

/**
 * 过磅记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
@Service
@RequiredArgsConstructor
public class MineWeightingServiceImpl implements MineWeightingService {

    private final MineWeightingMapper mineWeightingMapper;
    private final GenerateNoUtil generateNoUtil;

    /**
     * 查询过磅记录
     */
    @Override
    public MineWeightingVo queryById(Long id) {
        return mineWeightingMapper.selectVoById(id);
    }

    /**
     * 查询过磅记录列表
     */
    @Override
    public PageInfoRes<MineWeightingVo> queryPageList(MineWeightingDto dto, BasePage basePage) {
        LambdaQueryWrapper<MineWeighting> lqw = buildQueryWrapper(dto);
        Page<MineWeightingVo> result = mineWeightingMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询过磅记录列表
     */
    @Override
    public List<MineWeightingVo> queryList(MineWeightingDto dto) {
        LambdaQueryWrapper<MineWeighting> lqw = buildQueryWrapper(dto);
        return mineWeightingMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MineWeighting> buildQueryWrapper(MineWeightingDto dto) {
        LambdaQueryWrapper<MineWeighting> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineWeighting::getCarNumber, dto.getCarNumber());
        lqw.eq(StringUtils.isNotBlank(dto.getWeighingNo()), MineWeighting::getWeighingNo, dto.getWeighingNo());
        lqw.like(StringUtils.isNotBlank(dto.getGoodsName()), MineWeighting::getGoodsName, dto.getGoodsName());
        lqw.eq(dto.getShipMerchantId() != null, MineWeighting::getShipMerchantId, dto.getShipMerchantId());
        lqw.eq(dto.getShipTime() != null, MineWeighting::getShipTime, dto.getShipTime());
        lqw.eq(StringUtils.isNotBlank(dto.getShipAddress()), MineWeighting::getShipAddress, dto.getShipAddress());
        lqw.eq(StringUtils.isNotBlank(dto.getDeliveryMerchant()), MineWeighting::getDeliveryMerchant, dto.getDeliveryMerchant());
        lqw.eq(dto.getDeliveryTime() != null, MineWeighting::getDeliveryTime, dto.getDeliveryTime());
        lqw.eq(dto.getWeighingStatus() != null, MineWeighting::getWeighingStatus, dto.getWeighingStatus());
        lqw.eq(dto.getTotalWeight() != null, MineWeighting::getTotalWeight, dto.getTotalWeight());
        lqw.eq(dto.getTareWeight() != null, MineWeighting::getTareWeight, dto.getTareWeight());
        lqw.eq(dto.getNetWeight() != null, MineWeighting::getNetWeight, dto.getNetWeight());
        return lqw;
    }

    /**
     * 新增过磅记录
     */
    @Override
    public void insertByBo(MineWeightingDto dto) {
        MineWeighting add = MapstructUtils.convert(dto, MineWeighting.class);
        add.setWeighingNo(generateNoUtil.getBizNo(NoTypeEnum.WEIGHING_NO));
        mineWeightingMapper.insert(add);
    }

    /**
     * 修改过磅记录
     */
    @Override
    public void updateByBo(MineWeightingDto dto) {
        MineWeighting update = MapstructUtils.convert(dto, MineWeighting.class);
        mineWeightingMapper.updateById(update);
    }

    /**
     * 批量删除过磅记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineWeightingMapper.deleteBatchIds(ids);
    }

}
