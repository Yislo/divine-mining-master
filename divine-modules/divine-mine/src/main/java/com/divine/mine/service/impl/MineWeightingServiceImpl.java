package com.divine.mine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 过磅记录 Service
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
     * 过磅：录入车牌号、物资名称、发货单位、收货单位、发货日期、总重
     */
    @Override
    public void insertByBo(WeightingAddDto dto) {
        // 同一车牌号只能有一条未回磅记录
        List<MineWeighting> existList = mineWeightingMapper.selectList(
            new LambdaQueryWrapper<MineWeighting>()
                .eq(MineWeighting::getCarNumber, dto.getCarNumber())
                .eq(MineWeighting::getWeighingStatus, 0)
        );
        if (CollUtil.isNotEmpty(existList)) {
            throw new BusinessException("该车牌号还有未回磅记录，请先回磅或作废后再试");
        }
        MineWeighting add = MapstructUtils.convert(dto, MineWeighting.class);
        // 生成过磅单编号
        MerchantVo merchant = merchantService.queryById(dto.getShipMerchantId());
        add.setWeighingNo(generateNoUtil.getBizNo(merchant.getMerchantNo()));
        // 过磅状态：已过磅
        add.setWeighingStatus(0);
        // 过磅员和过磅时间
        add.setWeighingOperator(add.getCreateBy());
        add.setWeighingTime(LocalDateTime.now());
        mineWeightingMapper.insert(add);
    }

    /**
     * 回磅：输入皮重，自动计算净重 = 总重 - 皮重
     */
    @Override
    public void returnWeighting(WeightingReturnDto dto) {
        MineWeighting weighting = mineWeightingMapper.selectById(dto.getId());
        if (weighting == null) {
            throw new BusinessException("过磅记录不存在");
        }
        if (weighting.getWeighingStatus() != 0) {
            throw new BusinessException("该记录不是待回磅状态");
        }
        // 更新皮重、净重
        weighting.setTareWeight(dto.getTareWeight());
        weighting.setNetWeight(dto.getNetWeight());
        // 回磅状态：已回磅
        weighting.setWeighingStatus(1);
        // 回磅员和回磅时间
        weighting.setReturnTime(LocalDateTime.now());
        mineWeightingMapper.updateById(weighting);
    }

    /**
     * 根据车牌号查询未回磅车辆（回磅前置查询）
     */
    @Override
    public MineWeightingVo getReturnWeighting(String carNumber) {
        List<MineWeightingVo> list = mineWeightingMapper.selectVoList(
            new LambdaQueryWrapper<MineWeighting>()
                .eq(MineWeighting::getCarNumber, carNumber)
                .eq(MineWeighting::getWeighingStatus, 0)
        );
        if (CollUtil.isEmpty(list)) {
            throw new BusinessException("未查到该车辆的过磅记录，请先过磅");
        }
        if (list.size() > 1) {
            throw new BusinessException("该车辆有多条未回磅记录，请作废无效数据后重试");
        }
        return list.get(0);
    }

    /**
     * 查询详情
     */
    @Override
    public MineWeightingVo queryById(Long id) {
        MineWeightingVo vo = mineWeightingMapper.selectVoById(id);
        setMerchantNames(vo);
        return vo;
    }

    /**
     * 分页查询
     */
    @Override
    public PageInfoRes<MineWeightingVo> queryPageList(WeightingQueryDto dto) {
        LambdaQueryWrapper<MineWeighting> qw = new LambdaQueryWrapper<MineWeighting>()
            .like(StringUtils.isNotBlank(dto.getWeighingNo()), MineWeighting::getWeighingNo, dto.getWeighingNo())
            .like(StringUtils.isNotBlank(dto.getCarNumber()), MineWeighting::getCarNumber, dto.getCarNumber())
            .eq(ObjUtil.isNotNull(dto.getWeighingType()), MineWeighting::getWeighingType, dto.getWeighingType())
            .eq(ObjUtil.isNotNull(dto.getWeighingStatus()), MineWeighting::getWeighingStatus, dto.getWeighingStatus())
            .eq(ObjUtil.isNotNull(dto.getGoodsType()), MineWeighting::getGoodsType, dto.getGoodsType())
            .eq(ObjUtil.isNotNull(dto.getShipMerchantId()), MineWeighting::getShipMerchantId, dto.getShipMerchantId())
            .ge(StringUtils.isNotBlank(dto.getStartTime()), MineWeighting::getCreateTime, dto.getStartTime())
            .le(StringUtils.isNotBlank(dto.getEndTime()), MineWeighting::getCreateTime, dto.getEndTime())
            .orderByDesc(MineWeighting::getCreateTime);

        // 质量单查询：只查已回磅且未关联质量单的
        if (ObjUtil.isNotNull(dto.getIsQuality()) && dto.getIsQuality() == 1) {
            qw.isNull(MineWeighting::getQualityId);
            qw.ne(MineWeighting::getWeighingStatus, -1);
        }

        Page<MineWeightingVo> result = mineWeightingMapper.selectVoPage(dto.build(), qw);
        if (CollUtil.isNotEmpty(result.getRecords())) {
            setMerchantNames(result.getRecords());
        }
        return PageInfoRes.build(result);
    }

    /**
     * 列表查询（导出用）
     */
    @Override
    public List<MineWeightingVo> queryList(MineWeightingDto dto) {
        LambdaQueryWrapper<MineWeighting> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineWeighting::getCarNumber, dto.getCarNumber());
        lqw.eq(StringUtils.isNotBlank(dto.getWeighingNo()), MineWeighting::getWeighingNo, dto.getWeighingNo());
        lqw.eq(dto.getGoodsType() != null, MineWeighting::getGoodsType, dto.getGoodsType());
        lqw.eq(dto.getShipMerchantId() != null, MineWeighting::getShipMerchantId, dto.getShipMerchantId());
        lqw.eq(dto.getWeighingStatus() != null, MineWeighting::getWeighingStatus, dto.getWeighingStatus());
        List<MineWeightingVo> list = mineWeightingMapper.selectVoList(lqw);
        setMerchantNames(list);
        return list;
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
     * 查询未回磅的车牌号列表
     */
    @Override
    public List<String> getNotReturnCar() {
        List<MineWeighting> list = mineWeightingMapper.selectList(
            new LambdaQueryWrapper<MineWeighting>()
                .eq(MineWeighting::getWeighingStatus, 0)
        );
        return list.stream().map(MineWeighting::getCarNumber).distinct().toList();
    }

    /**
     * 作废
     */
    @Override
    public void invalid(Long id) {
        MineWeighting record = mineWeightingMapper.selectById(id);
        if (record == null) return;
        record.setWeighingStatus(-1);
        mineWeightingMapper.updateById(record);
    }

    /**
     * 批量删除
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineWeightingMapper.deleteBatchIds(ids);
    }

    // ===== 辅助方法 =====

    private void setMerchantNames(List<MineWeightingVo> list) {
        List<Long> merchantIds = list.stream()
            .flatMap(r -> {
                java.util.Set<Long> ids = new java.util.HashSet<>();
                if (r.getShipMerchantId() != null) ids.add(r.getShipMerchantId());
                if (r.getReceiptMerchantId() != null) ids.add(r.getReceiptMerchantId());
                return ids.stream();
            })
            .distinct()
            .toList();
        if (CollUtil.isEmpty(merchantIds)) return;
        List<MerchantVo> merchants = merchantService.queryByIds(merchantIds);
        Map<Long, String> nameMap = merchants.stream()
            .collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        list.forEach(r -> {
            if (r.getShipMerchantId() != null) r.setShipMerchantName(nameMap.get(r.getShipMerchantId()));
            if (r.getReceiptMerchantId() != null) r.setReceiptMerchantName(nameMap.get(r.getReceiptMerchantId()));
        });
    }

    private void setMerchantNames(MineWeightingVo vo) {
        if (vo.getShipMerchantId() != null) {
            MerchantVo m = merchantService.queryById(vo.getShipMerchantId());
            if (m != null) vo.setShipMerchantName(m.getMerchantName());
        }
        if (vo.getReceiptMerchantId() != null) {
            MerchantVo m = merchantService.queryById(vo.getReceiptMerchantId());
            if (m != null) vo.setReceiptMerchantName(m.getMerchantName());
        }
    }
}
