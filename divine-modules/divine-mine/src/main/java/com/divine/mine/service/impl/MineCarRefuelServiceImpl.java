package com.divine.mine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.enums.NoTypeEnum;
import com.divine.common.core.utils.GenerateNoUtil;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineCarRefuelDto;
import com.divine.mine.domain.entity.MineCarRefuel;
import com.divine.mine.domain.vo.MineCarRefuelVo;
import com.divine.mine.service.MineCarRefuelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineCarRefuelMapper;

import java.util.List;
import java.util.Collection;

/**
 * 车辆加油记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
@Service
@RequiredArgsConstructor
public class MineCarRefuelServiceImpl implements MineCarRefuelService {

    private final MineCarRefuelMapper mineCarRefuelMapper;
    private final GenerateNoUtil generateNoUtil;

    /**
     * 查询车辆加油记录
     */
    @Override
    public MineCarRefuelVo queryById(Long id) {
        return mineCarRefuelMapper.selectVoById(id);
    }

    /**
     * 查询车辆加油记录列表
     */
    @Override
    public PageInfoRes<MineCarRefuelVo> queryPageList(MineCarRefuelDto dto, BasePage basePage) {
        LambdaQueryWrapper<MineCarRefuel> lqw = buildQueryWrapper(dto);
        Page<MineCarRefuelVo> result = mineCarRefuelMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询车辆加油记录列表
     */
    @Override
    public List<MineCarRefuelVo> queryList(MineCarRefuelDto dto) {
        LambdaQueryWrapper<MineCarRefuel> lqw = buildQueryWrapper(dto);
        return mineCarRefuelMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MineCarRefuel> buildQueryWrapper(MineCarRefuelDto dto) {
        LambdaQueryWrapper<MineCarRefuel> lqw = Wrappers.lambdaQuery();
        lqw.eq(dto.getCarId() != null, MineCarRefuel::getCarId, dto.getCarId());
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineCarRefuel::getCarNumber, dto.getCarNumber());
        lqw.eq(StringUtils.isNotBlank(dto.getCarNo()), MineCarRefuel::getCarNo, dto.getCarNo());
        lqw.eq(dto.getIsExternal() != null, MineCarRefuel::getIsExternal, dto.getIsExternal());
        lqw.eq(dto.getOdometer() != null, MineCarRefuel::getOdometer, dto.getOdometer());
        lqw.eq(dto.getLitre() != null, MineCarRefuel::getLitre, dto.getLitre());
        lqw.eq(StringUtils.isNotBlank(dto.getRefuelType()), MineCarRefuel::getRefuelType, dto.getRefuelType());
        return lqw;
    }

    /**
     * 新增车辆加油记录
     */
    @Override
    public void insertByBo(MineCarRefuelDto dto) {
        MineCarRefuel add = MapstructUtils.convert(dto, MineCarRefuel.class);
        add.setRefuelNo(generateNoUtil.getBizNo(NoTypeEnum.REFUEL_NO));
        mineCarRefuelMapper.insert(add);
    }

    /**
     * 修改车辆加油记录
     */
    @Override
    public void updateByBo(MineCarRefuelDto dto) {
        MineCarRefuel update = MapstructUtils.convert(dto, MineCarRefuel.class);
        mineCarRefuelMapper.updateById(update);
    }

    /**
     * 批量删除车辆加油记录
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineCarRefuelMapper.deleteBatchIds(ids);
    }
}
