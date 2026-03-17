package com.divine.mine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineWeightingDto;
import com.divine.mine.domain.dto.WeightingAddDto;
import com.divine.mine.domain.dto.WeightingQueryDto;
import com.divine.mine.domain.dto.WeightingReturnDto;
import com.divine.mine.domain.entity.MineWeighting;
import com.divine.mine.domain.vo.MineWeightingVo;
import com.divine.mine.service.MineWeightingService;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineWeightingMapper;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final MerchantService merchantService;

    /**
     * 新增过磅记录
     */
    @Override
    public void insertByBo(WeightingAddDto dto) {
        List<MineWeighting> mineWeightings = mineWeightingMapper.selectList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getCarNumber, dto.getCarNumber())
            .eq(MineWeighting::getWeighingStatus, 0)
        );
        if (mineWeightings.size() > 1) {
            throw new BusinessException("还有未回磅记录，如果是无效数据，请作废后再试");
        }
        MineWeighting add = MapstructUtils.convert(dto, MineWeighting.class);
        // 获取合作单位信息
        MerchantVo merchantVo = merchantService.queryById(dto.getShipMerchantId());
        add.setWeighingNo(generateNoUtil.getBizNo(merchantVo.getMerchantNo()));
        add.setWeighingStatus(0);
        mineWeightingMapper.insert(add);
    }

    /**
     * 回磅
     *
     * @param dto
     */
    @Override
    public void returnWeighting(WeightingReturnDto dto) {
        MineWeighting weighting = BeanUtil.copyProperties(dto, MineWeighting.class);
        weighting.setWeighingStatus(1);
        mineWeightingMapper.updateById(weighting);
    }

    /**
     * 根据车牌号查询未回磅车辆
     *
     * @param carNumber
     * @return
     */
    @Override
    public MineWeightingVo getReturnWeighting(String carNumber) {
        List<MineWeightingVo> mineWeightings = mineWeightingMapper.selectVoList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getCarNumber, carNumber)
            .eq(MineWeighting::getWeighingStatus, 0)
        );
        if (CollUtil.isEmpty(mineWeightings)) {
            throw new BusinessException("车辆信息不存在");
        }
        if (mineWeightings.size() > 1) {
            throw new BusinessException("该车辆有多条未回磅记录，如果是无效数据，请作废后再试");
        }
        return mineWeightings.get(0);
    }

    /**
     * 查询过磅记录
     */
    @Override
    public MineWeightingVo queryById(Long id) {
        MineWeightingVo mineWeightingVo = mineWeightingMapper.selectVoById(id);
        // 获取合作单位信息
        MerchantVo merchantVo = merchantService.queryById(mineWeightingVo.getShipMerchantId());
        if (ObjUtil.isNotNull(merchantVo)) {
            mineWeightingVo.setShipMerchantName(merchantVo.getMerchantName());
        }
        return mineWeightingVo;
    }

    /**
     * 查询过磅记录列表
     */
    @Override
    public PageInfoRes<MineWeightingVo> queryPageList(WeightingQueryDto dto) {
        String weighingNo = dto.getWeighingNo();
        String carNumber = dto.getCarNumber();
        Integer weighingStatus = dto.getWeighingStatus();
        Integer goodsType = dto.getGoodsType();
        Long shipMerchantId = dto.getShipMerchantId();
        Integer isQuality = dto.getIsQuality();
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        LambdaQueryWrapper<MineWeighting> qw = new LambdaQueryWrapper<>(MineWeighting.class);
        qw.like(StringUtils.isNotBlank(weighingNo), MineWeighting::getWeighingNo, weighingNo)
            .like(StringUtils.isNotBlank(carNumber), MineWeighting::getCarNumber, carNumber)
            .eq(ObjUtil.isNotNull(weighingStatus), MineWeighting::getWeighingStatus, weighingStatus)
            .eq(ObjUtil.isNotNull(goodsType), MineWeighting::getGoodsType, goodsType)
            .eq(ObjUtil.isNotNull(shipMerchantId), MineWeighting::getShipMerchantId, shipMerchantId)
            .between(ObjUtil.isNotNull(startTime) && ObjUtil.isNotNull(endTime), MineWeighting::getCreateTime, startTime, endTime)
            .orderByDesc(MineWeighting::getCreateTime);
        if (ObjUtil.isNotNull(isQuality) && isQuality == 1) {
            qw.isNull(MineWeighting::getQualityId);
        }
        Page<MineWeightingVo> result = mineWeightingMapper.selectVoPage(dto.build(), qw);
        // set送货单位名称
        if (CollUtil.isNotEmpty(result.getRecords())) {
            setMerchantName(result.getRecords());
        }
        return PageInfoRes.build(result);
    }

    /**
     * 查询过磅记录列表
     */
    @Override
    public List<MineWeightingVo> queryList(MineWeightingDto dto) {
        LambdaQueryWrapper<MineWeighting> lqw = buildQueryWrapper(dto);
        List<MineWeightingVo> mineWeightingVos = mineWeightingMapper.selectVoList(lqw);
        // set送货单位名称
        setMerchantName(mineWeightingVos);
        return mineWeightingVos;
    }

    /**
     * set送货单位名称
     *
     * @param result
     */
    private void setMerchantName(List<MineWeightingVo> result) {
        //获取合作单位信息
        List<Long> merchantIds = result.stream().map(MineWeightingVo::getShipMerchantId).distinct().toList();
        List<MerchantVo> merchantVos = merchantService.queryByIds(merchantIds);
        Map<Long, String> merchantMap = merchantVos.stream().collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        result.forEach(r -> r.setShipMerchantName(merchantMap.get(r.getShipMerchantId())));
    }

    private LambdaQueryWrapper<MineWeighting> buildQueryWrapper(MineWeightingDto dto) {
        LambdaQueryWrapper<MineWeighting> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineWeighting::getCarNumber, dto.getCarNumber());
        lqw.eq(StringUtils.isNotBlank(dto.getWeighingNo()), MineWeighting::getWeighingNo, dto.getWeighingNo());
        lqw.like(dto.getGoodsType() != null, MineWeighting::getGoodsType, dto.getGoodsType());
        lqw.eq(dto.getShipMerchantId() != null, MineWeighting::getShipMerchantId, dto.getShipMerchantId());
        lqw.eq(dto.getShipTime() != null, MineWeighting::getShipTime, dto.getShipTime());
        lqw.eq(StringUtils.isNotBlank(dto.getShipAddress()), MineWeighting::getShipAddress, dto.getShipAddress());
        lqw.eq(StringUtils.isNotBlank(dto.getDeliveryMerchant()), MineWeighting::getDeliveryMerchant, dto.getDeliveryMerchant());
        lqw.eq(dto.getDeliveryTime() != null, MineWeighting::getDeliveryTime, dto.getDeliveryTime());
        lqw.eq(dto.getWeighingStatus() != null, MineWeighting::getWeighingStatus, dto.getWeighingStatus());
        return lqw;
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
     * 查询所有未回磅车辆
     */
    @Override
    public List<String> getNotReturnCar() {
        List<MineWeighting> mineWeightings = mineWeightingMapper.selectList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getWeighingStatus, 0));
        return mineWeightings.stream().map(MineWeighting::getCarNumber).toList();
    }

    /**
     * 作废
     *
     * @param id
     */
    @Override
    public void invalid(Long id) {
        MineWeighting mineWeighting = mineWeightingMapper.selectById(id);
        if (ObjUtil.isNull(mineWeighting)) {
            return;
        }
        mineWeighting.setWeighingStatus(-1);
        mineWeightingMapper.updateById(mineWeighting);

    }

    /**
     * 批量删除过磅记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineWeightingMapper.deleteBatchIds(ids);
    }

}
