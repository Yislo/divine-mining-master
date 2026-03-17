package com.divine.mine.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineWeightingDto;
import com.divine.mine.domain.dto.WeightingAddDto;
import com.divine.mine.domain.dto.WeightingQueryDto;
import com.divine.mine.domain.dto.WeightingReturnDto;
import com.divine.mine.domain.vo.MineWeightingVo;

import java.util.Collection;
import java.util.List;

/**
 * 过磅记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
public interface MineWeightingService {

    /**
     * 新增过磅记录
     */
    void insertByBo(WeightingAddDto dto);

    /**
     * 回磅
     *
     * @param dto
     */
    void returnWeighting(WeightingReturnDto dto);

    /**
     * 根据车牌号查询未回磅信息
     *
     * @param carNumber
     * @return
     */
    MineWeightingVo getReturnWeighting(String carNumber);

    /**
     * 查询过磅记录
     */
    MineWeightingVo queryById(Long id);

    /**
     * 查询过磅记录列表
     */
    PageInfoRes<MineWeightingVo> queryPageList(WeightingQueryDto dto);

    /**
     * 查询过磅记录列表
     */
    List<MineWeightingVo> queryList(MineWeightingDto dto);

    /**
     * 修改过磅记录
     */
    void updateByBo(MineWeightingDto dto);

    /**
     * 作废
     *
     * @param id
     */
    void invalid(Long id);

    /**
     * 查询未回磅车牌号
     *
     * @return
     */
    List<String> getNotReturnCar();

    /**
     * 批量删除过磅记录
     */
    void deleteByIds(Collection<Long> ids);
}
