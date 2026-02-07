package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.Item;
import com.divine.warehouse.domain.vo.ItemVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper extends BaseMapperPlus<Item, ItemVo> {

}
