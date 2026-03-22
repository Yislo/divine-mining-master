package com.divine.mine.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineAccessRecordDto;
import com.divine.mine.domain.entity.MineAccessRecord;
import com.divine.mine.domain.vo.MineAccessRecordVo;
import com.divine.mine.service.MineAccessRecordService;
import com.divine.warehouse.domain.entity.ReceiptOrder;
import com.divine.warehouse.domain.vo.MerchantVo;
import com.divine.warehouse.service.MerchantService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineAccessRecordMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;


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
    private final MerchantService merchantService;

    /**
     * 查询车辆出入厂记录
     */
    @Override
    public MineAccessRecordVo queryById(Long id) {
        return mineAccessRecordMapper.selectVoById(id);
    }

    /**
     * 查询车辆出入厂记录列表
     */
    @Override
    public PageInfoRes<MineAccessRecordVo> queryPageList(MineAccessRecordDto dto, BasePage basePage) {
        LambdaQueryWrapper<MineAccessRecord> lqw = buildQueryWrapper(dto);
        Page<MineAccessRecordVo> result = mineAccessRecordMapper.selectVoPage(basePage.build(), lqw);
        // 填充单位名称
        // 获取单位名称
        List<Long> list = result.getRecords().stream().map(MineAccessRecordVo::getMerchantId).toList();
        List<MerchantVo> merchantVos = merchantService.queryByIds(list);
        Map<Long, String> merchantMap = merchantVos.stream().collect(Collectors.toMap(MerchantVo::getId, MerchantVo::getMerchantName));
        result.getRecords().forEach(r -> r.setMerchantName(StringUtils.isBlank(merchantMap.get(r.getMerchantId())) ? "-" : merchantMap.get(r.getMerchantId())));
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
        lqw.ge(StringUtils.isNotBlank(dto.getStartTime()), MineAccessRecord::getCreateTime, dto.getStartTime());
        lqw.le(StringUtils.isNotBlank(dto.getEndTime()), MineAccessRecord::getCreateTime, dto.getEndTime());
        return lqw;
    }

    /**
     * 新增车辆出入厂记录
     */
    @Override
    public void insertByBo(MineAccessRecordDto dto) {
        MineAccessRecord add = MapstructUtils.convert(dto, MineAccessRecord.class);
        add.setStatus(1);
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
     * 出厂
     *
     * @param id
     */
    @Override
    public void out(Long id) {
        MineAccessRecord accessRecord = mineAccessRecordMapper.selectById(id);
        if (ObjUtil.isNull(accessRecord)) {
            throw new BusinessException("数据异常,请联系系统管理员");
        }
        accessRecord.setStatus(2);
        accessRecord.setExitTime(LocalDateTime.now());
        mineAccessRecordMapper.updateById(accessRecord);

    }

    /**
     * 批量删除车辆出入厂记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineAccessRecordMapper.deleteBatchIds(ids);
    }
}
