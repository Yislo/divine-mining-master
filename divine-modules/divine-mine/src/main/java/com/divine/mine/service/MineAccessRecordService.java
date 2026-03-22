package com.divine.mine.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.mine.domain.dto.MineAccessRecordDto;
import com.divine.mine.domain.vo.MineAccessRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * 车辆出入厂记录Service业务层处理
 *
 * @author yisl
 * @date 2026-02-28
 */
public interface MineAccessRecordService {


    /**
     * 查询车辆出入厂记录
     */
    MineAccessRecordVo queryById(Long id);

    /**
     * 查询车辆出入厂记录列表
     */
    PageInfoRes<MineAccessRecordVo> queryPageList(MineAccessRecordDto dto, BasePage basePage);

    /**
     * 查询车辆出入厂记录列表
     */
    List<MineAccessRecordVo> queryList(MineAccessRecordDto dto);

    /**
     * 新增车辆出入厂记录
     */
    void insertByBo(MineAccessRecordDto dto);

    /**
     * 修改车辆出入厂记录
     */
    void updateByBo(MineAccessRecordDto dto);

    /**
     * 出厂
     */
    void out(Long id);

    /**
     * 批量删除车辆出入厂记录
     */
    void deleteByIds(Collection<Long> ids);
}
