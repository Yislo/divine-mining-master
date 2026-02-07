package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ItemBrand;
import com.divine.warehouse.domain.vo.ItemBrandVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品品牌Mapper接口
 *
 * @author zcc
 * @date 2024-07-30
 */
@Mapper
public interface ItemBrandMapper extends BaseMapperPlus<ItemBrand, ItemBrandVo> {

}
