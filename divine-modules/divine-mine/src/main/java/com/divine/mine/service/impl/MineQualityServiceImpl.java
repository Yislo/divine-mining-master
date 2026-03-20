package com.divine.mine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.divine.mine.domain.dto.MineQualityDto;
import com.divine.mine.domain.dto.QualityPageDTO;
import com.divine.mine.domain.entity.MineQuality;
import com.divine.mine.domain.entity.MineWeighting;
import com.divine.mine.domain.vo.MineQualityInfoVo;
import com.divine.mine.domain.vo.MineQualityVo;
import com.divine.mine.domain.vo.MineWeightingVo;
import com.divine.mine.domain.vo.QualityExcelVo;
import com.divine.mine.mapper.MineWeightingMapper;
import com.divine.mine.service.MineQualityService;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineQualityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 送货质量Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
@Service
@RequiredArgsConstructor
public class MineQualityServiceImpl implements MineQualityService {

    private final MineQualityMapper qualityMapper;
    private final MineWeightingMapper weightingMapper;
    private final GenerateNoUtil generateNoUtil;
    private final MerchantService merchantService;

    /**
     * 新增送货质量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByDto(MineQualityDto dto) {
        List<Long> weightingId = dto.getWeightingId();
        if (CollUtil.isEmpty(weightingId)) {
            throw new BusinessException("质量单不能为空");
        }
        List<MineWeighting> mineWeightings = weightingMapper.selectList(new LambdaQueryWrapper<>(MineWeighting.class)
            .in(MineWeighting::getId, weightingId));
        boolean b = mineWeightings.stream()
            .map(MineWeighting::getShipMerchantId)
            .distinct()
            .count() == 1;
        if (!b) {
            throw new BusinessException("送货单位不一致");
        }
        MineQuality mineQuality = MapstructUtils.convert(dto, MineQuality.class);
        mineQuality.setQualityNo(generateNoUtil.getBizNo(NoTypeEnum.QUALITY_NO.getCode()));
        mineQuality.setShipMerchantId(mineWeightings.get(0).getShipMerchantId());
        qualityMapper.insert(mineQuality);
        // 填充过磅记录质量id
        updateWeighting(mineQuality.getId(), weightingId);
    }

    /**
     * 更新过磅单
     *
     * @param qualityId
     * @param weightingId
     */
    private void updateWeighting(Long qualityId, List<Long> weightingId) {
        //清除原来的绑定记录
        weightingMapper.update(null, new LambdaUpdateWrapper<MineWeighting>()
            .set(MineWeighting::getQualityId, null)
            .eq(MineWeighting::getQualityId, qualityId));
        // 填充过磅记录质量id
        if (CollUtil.isNotEmpty(weightingId)) {
            List<MineWeighting> list = weightingId.stream().map(id -> {
                MineWeighting mineWeighting = new MineWeighting();
                mineWeighting.setId(id);
                mineWeighting.setQualityId(qualityId);
                return mineWeighting;
            }).toList();
            weightingMapper.updateBatchById(list);
        }
    }


    /**
     * 查询送货质量
     */
    @Override
    public MineQualityInfoVo queryById(Long id) {
        // 查询质量
        MineQualityVo mineQualityVo = qualityMapper.selectVoById(id);
        MineQualityInfoVo mineQualityInfoVo = BeanUtil.copyProperties(mineQualityVo, MineQualityInfoVo.class);
        List<MineWeightingVo> mineWeightingVos = weightingMapper.selectVoList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getQualityId, id));
        MerchantVo merchantVo = merchantService.queryById(mineQualityVo.getShipMerchantId());
        // 前置限制 同一个发货单位才能为一组数据
        mineWeightingVos.forEach(w -> w.setShipMerchantName(merchantVo.getMerchantName()));
        mineQualityInfoVo.setWeightingList(mineWeightingVos);
        return mineQualityInfoVo;
    }

    /**
     * 查询送货质量列表
     */
    @Override
    public PageInfoRes<MineQualityVo> queryPageList(QualityPageDTO dto) {
        // 组装条件查询
        IPage<MineQualityVo> res = qualityMapper.selectVoPage(dto.build(), getQw(dto));
        // 获取单位名称
        List<Long> list = res.getRecords().stream().map(MineQualityVo::getShipMerchantId).toList();
        List<MerchantVo> merchantVos = merchantService.queryByIds(list);
        Map<Long, String> merchantMap = merchantVos.stream().collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        res.getRecords().forEach(r -> r.setShipMerchantName(merchantMap.get(r.getShipMerchantId())));
        return PageInfoRes.build(res);
    }

    /**
     * 组装查询条件
     *
     * @param dto
     * @return
     */
    private LambdaQueryWrapper<MineQuality> getQw(QualityPageDTO dto) {
        String qualityNo = dto.getQualityNo();
        Long shipMerchantId = dto.getShipMerchantId();
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        return new LambdaQueryWrapper<>(MineQuality.class)
            .like(StringUtils.isNotBlank(qualityNo), MineQuality::getQualityNo, qualityNo)
            .like(ObjUtil.isNotNull(shipMerchantId), MineQuality::getShipMerchantId, shipMerchantId)
            .ge(StringUtils.isNotBlank(startTime), MineQuality::getCreateTime, startTime)
            .le(StringUtils.isNotBlank(endTime), MineQuality::getCreateTime, endTime)
            .orderByDesc(MineQuality::getCreateTime);

    }

    /**
     * 查询送货质量列表
     */
    @Override
    public List<QualityExcelVo> queryList(QualityPageDTO dto) {
        // 查询质量单
        List<MineQualityVo> mineQualityVos = qualityMapper.selectVoList(getQw(dto));
        List<Long> merchantIds = mineQualityVos.stream().map(MineQualityVo::getShipMerchantId).distinct().toList();
        List<Long> qualityIds = mineQualityVos.stream().map(MineQualityVo::getId).toList();
        // 获取过磅单信息
        List<MineWeighting> mineWeightings = weightingMapper.selectList(new LambdaQueryWrapper<>(MineWeighting.class)
            .in(MineWeighting::getQualityId, qualityIds));
        // 获取单位名称
        List<MerchantVo> merchantVos = merchantService.queryByIds(merchantIds);
        Map<Long, String> merchantMap = merchantVos.stream().collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        mineQualityVos.forEach(r -> r.setShipMerchantName(merchantMap.get(r.getShipMerchantId())));

        Map<Long, MineQualityVo> qualityMap = mineQualityVos.stream().collect(Collectors.toMap(MineQualityVo::getId, Function.identity()));
        List<QualityExcelVo> excelVos = BeanUtil.copyToList(mineWeightings, QualityExcelVo.class);
        excelVos.forEach(qualityExcelVo -> {
            MineQualityVo mineQualityVo = qualityMap.get(qualityExcelVo.getQualityId());
            qualityExcelVo.setAcidDemand(mineQualityVo.getAcidDemand());
            qualityExcelVo.setMoisture(mineQualityVo.getMoisture());
            qualityExcelVo.setCuoRatio(mineQualityVo.getCuoRatio());
            qualityExcelVo.setQualityStatus(mineQualityVo.getQualityStatus().toString());
            qualityExcelVo.setQualityNo(mineQualityVo.getQualityNo());
            qualityExcelVo.setShipMerchantName(mineQualityVo.getShipMerchantName());
            qualityExcelVo.setCreateTime(mineQualityVo.getCreateTime());
        });


        return excelVos;
    }

    /**
     * 修改送货质量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByBo(MineQualityDto dto) {
        MineQuality update = MapstructUtils.convert(dto, MineQuality.class);
        qualityMapper.updateById(update);
        updateWeighting(dto.getId(), dto.getWeightingId());
    }

    /**
     * 作废
     *
     * @param id
     */
    @Override
    public void invalid(Long id) {
        MineQuality mineQuality = qualityMapper.selectById(id);
        if (ObjUtil.isNull(mineQuality)) {
            throw new BusinessException("质量单不存在");
        }
        mineQuality.setQualityStatus(-1);
        qualityMapper.updateById(mineQuality);
        //更新过磅单
        weightingMapper.update(null, new LambdaUpdateWrapper<MineWeighting>()
            .set(MineWeighting::getQualityId, null)
            .eq(MineWeighting::getQualityId, id));
    }

    /**
     * 批量删除送货质量
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        qualityMapper.deleteBatchIds(ids);
    }
}
