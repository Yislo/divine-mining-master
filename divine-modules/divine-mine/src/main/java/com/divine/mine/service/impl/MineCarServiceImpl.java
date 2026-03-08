package com.divine.mine.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.mybatis.core.page.BasePage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.divine.mine.domain.dto.MineCarDto;
import com.divine.mine.domain.entity.MineCar;
import com.divine.mine.domain.vo.MineCarVo;
import com.divine.mine.service.MineCarService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.divine.mine.mapper.MineCarMapper;

import java.util.List;
import java.util.Collection;

/**
 * 车辆信息Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
@Service
@RequiredArgsConstructor
public class MineCarServiceImpl implements MineCarService {

    private final MineCarMapper mineCarMapper;

    /**
     * 查询车辆信息
     */
    @Override
    public MineCarVo queryById(Long id){
        return mineCarMapper.selectVoById(id);
    }

    /**
     * 查询车辆信息列表
     */
    @Override
    public PageInfoRes<MineCarVo> queryPageList(MineCarDto dto, BasePage basePage) {
        LambdaQueryWrapper<MineCar> lqw = buildQueryWrapper(dto);
        Page<MineCarVo> result = mineCarMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(result);
    }

    /**
     * 查询车辆信息列表
     */
    @Override
    public List<MineCarVo> queryList(MineCarDto dto) {
        LambdaQueryWrapper<MineCar> lqw = buildQueryWrapper(dto);
        return mineCarMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<MineCar> buildQueryWrapper(MineCarDto dto) {
        LambdaQueryWrapper<MineCar> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(dto.getCarNumber()), MineCar::getCarNumber, dto.getCarNumber());
        lqw.eq(StringUtils.isNotBlank(dto.getCarNo()), MineCar::getCarNo, dto.getCarNo());
        lqw.eq(StringUtils.isNotBlank(dto.getDriverPhone()), MineCar::getDriverPhone, dto.getDriverPhone());
        lqw.eq(StringUtils.isNotBlank(dto.getDriver()), MineCar::getDriver, dto.getDriver());
        lqw.eq(dto.getCarType() != null, MineCar::getCarType, dto.getCarType());
        lqw.eq(dto.getIsDel() != null, MineCar::getIsDel, dto.getIsDel());
        return lqw;
    }

    /**
     * 新增车辆信息
     */
    @Override
    public void insertByBo(MineCarDto dto) {
        MineCar add = MapstructUtils.convert(dto, MineCar.class);
        mineCarMapper.insert(add);
    }

    /**
     * 修改车辆信息
     */
    @Override
    public void updateByBo(MineCarDto dto) {
        MineCar update = MapstructUtils.convert(dto, MineCar.class);
        mineCarMapper.updateById(update);
    }

    /**
     * 批量删除车辆信息
     */
    @Override
    public void deleteByIds(Collection<Long> ids) {
        mineCarMapper.deleteBatchIds(ids);
    }
}
