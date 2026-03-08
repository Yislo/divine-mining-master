package com.divine.mine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineAccessRecordDto;
import com.divine.mine.domain.entity.MineAccessRecord;
import com.divine.mine.domain.vo.MineAccessRecordVo;
import com.divine.mine.service.MineAccessRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineAccessRecordMapper;

import java.util.List;
import java.util.Collection;

/**
 * 车辆出入厂记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
@Service
@RequiredArgsConstructor
public class MineAccessRecordServiceImpl implements MineAccessRecordService {

    private final MineAccessRecordMapper mineAccessRecordMapper;

    /**
     * 查询车辆出入厂记录
     */
    @Override
    public MineAccessRecordVo queryById(Long id){
        return mineAccessRecordMapper.selectVoById(id);
    }

    /**
     * 查询车辆出入厂记录列表
     */
    @Override
    public PageInfoRes<MineAccessRecordVo> queryPageList(MineAccessRecordDto dto, BasePage basePage) {
        LambdaQueryWrapper<MineAccessRecord> lqw = buildQueryWrapper(dto);
        Page<MineAccessRecordVo> result = mineAccessRecordMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询车辆出入厂记录列表
     */
    @Override
    public List<MineAccessRecordVo> queryList(MineAccessRecordDto dto) {
        LambdaQueryWrapper<MineAccessRecord> lqw = buildQueryWrapper(dto);
        return mineAccessRecordMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MineAccessRecord> buildQueryWrapper(MineAccessRecordDto dto) {
        LambdaQueryWrapper<MineAccessRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineAccessRecord::getCarNumber, dto.getCarNumber());
        lqw.eq(dto.getEnterTime() != null, MineAccessRecord::getEnterTime, dto.getEnterTime());
        lqw.eq(dto.getExitTime() != null, MineAccessRecord::getExitTime, dto.getExitTime());
        lqw.eq(dto.getMerchantId() != null, MineAccessRecord::getMerchantId, dto.getMerchantId());
        lqw.eq(dto.getEntryType() != null, MineAccessRecord::getEntryType, dto.getEntryType());
        lqw.eq(dto.getStatus() != null, MineAccessRecord::getStatus, dto.getStatus());
        return lqw;
    }

    /**
     * 新增车辆出入厂记录
     */
    @Override
    public void insertByBo(MineAccessRecordDto dto) {
        MineAccessRecord add = MapstructUtils.convert(dto, MineAccessRecord.class);
        mineAccessRecordMapper.insert(add);
    }

    /**
     * 修改车辆出入厂记录
     */
    @Override
    public void updateByBo(MineAccessRecordDto dto) {
        MineAccessRecord update = MapstructUtils.convert(dto, MineAccessRecord.class);
        mineAccessRecordMapper.updateById(update);
    }

    /**
     * 批量删除车辆出入厂记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineAccessRecordMapper.deleteBatchIds(ids);
    }
}
