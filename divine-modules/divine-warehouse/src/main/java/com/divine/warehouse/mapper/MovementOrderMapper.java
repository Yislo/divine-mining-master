package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.MovementOrder;
import com.divine.warehouse.domain.vo.MovementOrderVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 移库单Mapper接口
 *
 * @author zcc
 * @date 2024-08-09
 */
@Mapper
public interface MovementOrderMapper extends BaseMapperPlus<MovementOrder, MovementOrderVo> {

}
