package com.divine.warehouse.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.warehouse.domain.entity.MovementOrderDetail;
import com.divine.warehouse.domain.vo.MoveDetailVO;
import com.divine.warehouse.domain.vo.MovementOrderDetailVO;
import com.divine.common.mybatis.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存移动详情Mapper接口
 *
 * @author yisl
 * @date 2024-08-09
 */
@Mapper
public interface MovementOrderDetailMapper extends BaseMapperPlus<MovementOrderDetail, MovementOrderDetailVO> {

    /**
     * 查询移库单详情列表
     *
     * @param page
     * @param moveId 移库单id
     * @return
     */
    IPage<MoveDetailVO> getMoveDetailList(IPage<MoveDetailVO> page, Long moveId);

}
