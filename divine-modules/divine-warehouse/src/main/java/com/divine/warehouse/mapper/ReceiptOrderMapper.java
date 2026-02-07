package com.divine.warehouse.mapper;

import com.divine.warehouse.domain.entity.ReceiptOrder;
import com.divine.warehouse.domain.vo.ReceiptOrderVo;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单Mapper接口
 *
 * @author zcc
 * @date 2024-07-19
 */
@Mapper
public interface ReceiptOrderMapper extends BaseMapperPlus<ReceiptOrder, ReceiptOrderVo> {

}
