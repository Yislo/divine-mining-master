package com.divine.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.divine.common.core.constant.CacheNames;
import com.divine.common.core.exception.ServiceException;
import com.divine.common.core.utils.MapstructUtils;
import com.divine.common.core.utils.StringUtils;
import com.divine.common.mybatis.core.page.BasePage;
import com.divine.common.mybatis.core.page.PageInfoRes;
import com.divine.common.redis.utils.CacheUtils;
import com.divine.system.domain.dto.SysDictDataDto;
import com.divine.system.domain.entity.SysDictData;
import com.divine.system.domain.vo.SysDictDataVo;
import com.divine.system.mapper.SysDictDataMapper;
import com.divine.system.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service
public class SysDictDataServiceImpl implements SysDictDataService {

    private final SysDictDataMapper dictDataMapper;

    @Override
    public PageInfoRes<SysDictDataVo> selectPageDictDataList(SysDictDataDto dictData, BasePage basePage) {
        LambdaQueryWrapper<SysDictData> lqw = buildQueryWrapper(dictData);
        Page<SysDictDataVo> page = dictDataMapper.selectVoPage(basePage.build(), lqw);
        return PageInfoRes.build(page);
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictDataVo> selectDictDataList(SysDictDataDto dictData) {
        LambdaQueryWrapper<SysDictData> lqw = buildQueryWrapper(dictData);
        return dictDataMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysDictData> buildQueryWrapper(SysDictDataDto bo) {
        LambdaQueryWrapper<SysDictData> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDictSort() != null, SysDictData::getDictSort, bo.getDictSort());
        lqw.like(StringUtils.isNotBlank(bo.getDictLabel()), SysDictData::getDictLabel, bo.getDictLabel());
        lqw.eq(StringUtils.isNotBlank(bo.getDictType()), SysDictData::getDictType, bo.getDictType());
        lqw.orderByAsc(SysDictData::getDictSort);
        return lqw;
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictDataVo selectDictDataById(Long dictCode) {
        return dictDataMapper.selectVoById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = dictDataMapper.selectById(dictCode);
            dictDataMapper.deleteById(dictCode);
            CacheUtils.evict(CacheNames.SYS_DICT, data.getDictType());
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    @Override
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#bo.dictType")
    public List<SysDictDataVo> insertDictData(SysDictDataDto bo) {
        SysDictData data = MapstructUtils.convert(bo, SysDictData.class);
        int row = dictDataMapper.insert(data);
        if (row > 0) {
            return dictDataMapper.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    @Override
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#bo.dictType")
    public List<SysDictDataVo> updateDictData(SysDictDataDto bo) {
        SysDictData data = MapstructUtils.convert(bo, SysDictData.class);
        int row = dictDataMapper.updateById(data);
        if (row > 0) {
            return dictDataMapper.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");
    }

    @Override
    public boolean checkDictDataUnique(SysDictDataDto dict) {
        Long dictCode = ObjectUtil.isNull(dict.getDictCode()) ? -1L : dict.getDictCode();
        SysDictData entity = dictDataMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
            .eq(SysDictData::getDictType, dict.getDictType()).eq(SysDictData::getDictValue, dict.getDictValue()));
        if (ObjectUtil.isNotNull(entity) && !dictCode.equals(entity.getDictCode())) {
            return false;
        }
        return true;
    }

}
