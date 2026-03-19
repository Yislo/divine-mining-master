package com.divine.mine.service;

import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineQualityDto;
import com.divine.mine.domain.dto.QualityPageDTO;
import com.divine.mine.domain.vo.MineQualityInfoVo;
import com.divine.mine.domain.vo.MineQualityVo;
import com.divine.mine.domain.vo.QualityExcelVo;

import java.util.Collection;
import java.util.List;

/**
 * 送货质量Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
public interface MineQualityService {

    /**
     * 新增送货质量
     */
    void insertByDto(MineQualityDto dto);

    /**
     * 查询送货质量
     */
    MineQualityInfoVo queryById(Long id);

    /**
     * 查询送货质量列表
     */
    PageInfoRes<MineQualityVo> queryPageList(QualityPageDTO dto);

    /**
     * 查询送货质量列表
     */
    List<QualityExcelVo> queryList(QualityPageDTO dto);
    /**
     * 修改送货质量
     */
    void updateByBo(MineQualityDto dto);

    /**
     * 作废
     * @param id
     */
    void invalid(Long id);

    /**
     * 批量删除送货质量
     */
    void deleteByIds(Collection<Long> ids);
}
