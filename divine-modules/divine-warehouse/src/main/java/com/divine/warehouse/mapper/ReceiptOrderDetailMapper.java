package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单详情Mapper接口
 *
 * @author zcc
 * @date 2024-07-19
 */
@Mapper
public interface ReceiptOrderDetailMapper extends BaseMapperPlus<ReceiptOrderDetail, ReceiptOrderDetailVo> {

}
