package com.divine.weighing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.refuel.RefuelPageDTO;
import com.divine.weighing.domain.entity.RefuelRecord;
import com.divine.weighing.domain.vo.home.MotorcadeRefuelStatisticsVO;
import com.divine.weighing.domain.vo.refuel.RefuelPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 加油记录持久层
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Mapper
public interface RefuelRecordMapper extends BaseMapper<RefuelRecord> {

    /**
     * 分页查询加油记录
     * @param page 分页参数
     * @param dto 查询条件
     * @return 加油记录
     */
    IPage<RefuelPageVO> queryRefuelPage(IPage<RefuelPageVO> page, @Param("dto") RefuelPageDTO dto);

    /**
     * 车队加油数据统计
     * @param dto
     * @return
     */
    List<MotorcadeRefuelStatisticsVO> motorcadeRefuelStatist(@Param("dto") DateDTO dto);
}
