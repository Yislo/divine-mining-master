package com.divine.mine.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.enums.NoTypeEnum;
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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineQualityMapper;

import java.util.Date;
import java.util.List;
import java.util.Collection;

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

    /**
     * 新增送货质量
     */
    @Override
    public void insertByDto(MineQualityDto dto) {
        MineQuality add = MapstructUtils.convert(dto, MineQuality.class);
        add.setQualityNo(generateNoUtil.getBizNo(NoTypeEnum.QUALITY_NO.getCode()));
        mineQualityMapper.insert(add);
        // 填充过磅记录质量id
        List<Long> weightingId = dto.getWeightingId();
        List<MineWeighting> list = weightingId.stream().map(id -> {
            MineWeighting mineWeighting = new MineWeighting();
            mineWeighting.setId(id);
            mineWeighting.setQualityId(add.getId());
            return mineWeighting;
        }).toList();
        mineWeightingMapper.updateBatchById(list);
    }

    /**
     * 查询送货质量
     */
    public MineQualityInfoVo queryById(Long id) {
        // 查询质量
        List<MineWeightingVo> mineWeightingVos = mineWeightingMapper.selectVoList(new LambdaQueryWrapper<>(MineWeighting.class)
            .eq(MineWeighting::getQualityId, id));
        MineQualityVo mineQualityVo = mineQualityMapper.selectVoById(id);
        MineQualityInfoVo mineQualityInfoVo = BeanUtil.copyProperties(mineQualityVo, MineQualityInfoVo.class);
        mineQualityInfoVo.setWeightingList(mineWeightingVos);
        return mineQualityInfoVo;
    }

    /**
     * 查询送货质量列表
     */
    public PageInfoRes<MineQualityVo> queryPageList(QualityPageDTO dto) {
        String weightingNo = dto.getWeightingNo();
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
     * 批量删除送货质量
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineQualityMapper.deleteBatchIds(ids);
    }
}
