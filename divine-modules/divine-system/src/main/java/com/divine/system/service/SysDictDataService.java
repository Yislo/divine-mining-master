package com.divine.system.service;

import com.divine.common.mybatis.core.page.BasePage;
import com.divine.system.domain.dto.SysDictDataDto;
import com.divine.system.domain.vo.SysDictDataVo;
import com.divine.common.mybatis.core.page.PageInfoRes;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author Lion Li
 */
public interface SysDictDataService {


    PageInfoRes<SysDictDataVo> selectPageDictDataList(SysDictDataDto dictData, BasePage basePage);

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    List<SysDictDataVo> selectDictDataList(SysDictDataDto dictData);

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    SysDictDataVo selectDictDataById(Long dictCode);

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    void deleteDictDataByIds(Long[] dictCodes);

    /**
     * 新增保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    List<SysDictDataVo> insertDictData(SysDictDataDto bo);

    /**
     * 修改保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    List<SysDictDataVo> updateDictData(SysDictDataDto bo);

    boolean checkDictDataUnique(SysDictDataDto dict);

}
