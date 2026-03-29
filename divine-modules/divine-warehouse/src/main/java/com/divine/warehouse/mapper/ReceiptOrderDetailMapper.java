package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.warehouse.domain.dto.ReceiptInfoDto;
import com.divine.warehouse.domain.entity.ReceiptOrderDetail;
import com.divine.warehouse.domain.vo.ReceiptInfoVO;
import com.divine.warehouse.domain.vo.ReceiptOrderDetailVO;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单详情Mapper接口
 *
 * @author yisl
 * @date 2024-07-19
 */
@Mapper
public interface ReceiptOrderDetailMapper extends BaseMapperPlus<ReceiptOrderDetail, ReceiptOrderDetailVO> {

    /**
     * 查询入库单详情列表
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<ReceiptInfoVO> getReceiptDetailList(IPage<ReceiptInfoVO> page, ReceiptInfoDto dto);


}
