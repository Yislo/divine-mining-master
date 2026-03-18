package com.divine.mine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
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
import com.divine.mine.mapper.MineWeightingMapper;
import com.divine.mine.service.MineQualityService;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineQualityMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.Map;
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

    private final MineQualityMapper mineQualityMapper;
    private final MineWeightingMapper mineWeightingMapper;
    private final GenerateNoUtil generateNoUtil;
    private final MerchantService merchantService;

    /**
     * 新增送货质量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertByDto(MineQualityDto dto) {
        List<MineWeighting> mineWeightings = mineWeightingMapper.selectList(new LambdaQueryWrapper<>(MineWeighting.class)
            .in(MineWeighting::getId, dto.getWeightingId()));
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
        mineQualityMapper.insert(mineQuality);
        // 填充过磅记录质量id
        List<Long> weightingId = dto.getWeightingId();
        List<MineWeighting> list = weightingId.stream().map(id -> {
            MineWeighting mineWeighting = new MineWeighting();
            mineWeighting.setId(id);
            mineWeighting.setQualityId(mineQuality.getId());
            return mineWeighting;
        }).toList();
        mineWeightingMapper.updateBatchById(list);
    }

    /**
     * 查询送货质量
     */
    @Override
    public MineQualityInfoVo queryById(Long id) {
        // 查询质量
        MineQualityVo mineQualityVo = mineQualityMapper.selectVoById(id);
        MineQualityInfoVo mineQualityInfoVo = BeanUtil.copyProperties(mineQualityVo, MineQualityInfoVo.class);
        List<MineWeightingVo> mineWeightingVos = mineWeightingMapper.selectVoList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getQualityId, id));
        MerchantVo merchantVo = merchantService.queryById(mineQualityVo.getShipMerchantId());
        // 前置限制 同一个发货单位才能为一组数据
        mineWeightingVos.forEach(w->w.setShipMerchantName(merchantVo.getMerchantName()));
        mineQualityInfoVo.setWeightingList(mineWeightingVos);
        return mineQualityInfoVo;
    }

    /**
     * 查询送货质量列表
     */
    @Override
    public PageInfoRes<MineQualityVo> queryPageList(QualityPageDTO dto) {
        String qualityNo = dto.getQualityNo();
        Long shipMerchantId = dto.getShipMerchantId();
        Date startTime = dto.getStartTime();
        Date endTime = dto.getEndTime();
        // 组装条件查询
        IPage<MineQualityVo> res = mineQualityMapper.selectVoPage(dto.build(), new LambdaQueryWrapper<>(MineQuality.class)
            .like(StringUtils.isNotBlank(qualityNo), MineQuality::getQualityNo, qualityNo)
            .like(ObjUtil.isNotNull(shipMerchantId), MineQuality::getShipMerchantId, shipMerchantId)
            .between(ObjUtil.isNotNull(startTime) || ObjUtil.isNotNull(endTime),
                MineQuality::getCreateTime, startTime, endTime)
        );
        List<Long> list = res.getRecords().stream().map(MineQualityVo::getShipMerchantId).toList();
        List<MerchantVo> merchantVos = merchantService.queryByIds(list);
        Map<Long, String> merchantMap = merchantVos.stream().collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        res.getRecords().forEach(r -> r.setShipMerchantName(merchantMap.get(r.getShipMerchantId())));
        return PageInfoRes.build(res);
    }

    /**
     * 查询送货质量列表
     */
    @Override
    public List<MineQualityVo> queryList(MineQualityDto dto) {
        return mineQualityMapper.selectVoList(new LambdaQueryWrapper<>());
    }

    /**
     * 修改送货质量
     */
    @Override
    public void updateByBo(MineQualityDto dto) {
        MineQuality update = MapstructUtils.convert(dto, MineQuality.class);
        mineQualityMapper.updateById(update);
    }

    /**
     * 作废
     *
     * @param id
     */
    @Override
    public void invalid(Long id) {
        MineQuality mineQuality = mineQualityMapper.selectById(id);
        if (ObjUtil.isNull(mineQuality)) {
            throw new BusinessException("质量单不存在");
        }
        mineQuality.setQualityStatus(-1);
        mineQualityMapper.updateById(mineQuality);
    }

    /**
     * 批量删除送货质量
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineQualityMapper.deleteBatchIds(ids);
    }
}
