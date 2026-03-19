package com.divine.mine.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineCarRefuelDto;
import com.divine.mine.domain.vo.CarRefuelExcelVo;
import com.divine.mine.domain.vo.MineCarRefuelVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 车辆加油记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
public interface MineCarRefuelService {

    /**
     * 查询车辆加油记录
     */
    MineCarRefuelVo queryById(Long id);

    /**
     * 查询车辆加油记录列表
     */
    PageInfoRes<MineCarRefuelVo> queryPageList(MineCarRefuelDto dto, BasePage basePage);

    /**
     * 查询车辆加油记录列表
     */
    List<CarRefuelExcelVo> queryList(MineCarRefuelDto dto);

    /**
     * 新增车辆加油记录
     */
    void insertByBo(MineCarRefuelDto dto);

    /**
     * 修改车辆加油记录
     */
    void updateByBo(MineCarRefuelDto dto);

    /**
     * 作废
     * @param id
     */
    void ts(Long id);
}
