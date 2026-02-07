package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.CheckOrderDetail;
import com.divine.warehouse.domain.vo.CheckOrderDetailVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存盘点单据详情Mapper接口
 *
 * @author zcc
 * @date 2024-08-13
 */
@Mapper
public interface CheckOrderDetailMapper extends BaseMapperPlus<CheckOrderDetail, CheckOrderDetailVo> {

}
