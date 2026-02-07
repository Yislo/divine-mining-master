package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ItemCategory;
import com.divine.warehouse.domain.vo.ItemCategoryVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemCategoryMapper extends BaseMapperPlus<ItemCategory, ItemCategoryVo> {
}
