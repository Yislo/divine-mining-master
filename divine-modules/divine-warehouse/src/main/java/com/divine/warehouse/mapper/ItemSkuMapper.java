package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.warehouse.domain.dto.ItemSkuDto;
import com.divine.warehouse.domain.entity.ItemSku;
import com.divine.warehouse.domain.vo.ItemSkuMapVo;
import com.divine.warehouse.domain.vo.ItemSkuVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ItemSkuMapper extends BaseMapperPlus<ItemSku, ItemSkuVo> {

    IPage<ItemSkuMapVo> selectByBo(IPage<ItemSkuVo> page, @Param("bo") ItemSkuDto bo);

    List<ItemSkuMapVo> queryItemSkuMapVos(Collection<Long> ids);

    ItemSkuMapVo queryItemSkuMapVo(Long id);
}
