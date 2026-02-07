package com.divine.weighing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.entity.DeliveryQuality;
import com.divine.weighing.domain.vo.deliveryquality.QualityPageVO;
import com.divine.weighing.domain.vo.deliveryquality.QualityStatisticsVO;
import com.divine.weighing.domain.vo.home.CompanyStatisticsVO;
import com.divine.weighing.domain.vo.home.MotorcadeStatisticsVO;
import com.divine.weighing.domain.dto.deliveryquality.QualityPageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 送货质量持久层
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Mapper
public interface DeliveryQualityMapper extends BaseMapper<DeliveryQuality> {


    /**
     * 分页查询质量
     *
     * @param page
     * @param dto
     * @return
     */
    IPage<QualityPageVO> queryQualityPage(IPage<QualityPageVO> page, @Param("dto") QualityPageDTO dto);

    /**
     * 查询工厂送货统计
     *
     * @param dto
     * @return
     */
    List<CompanyStatisticsVO> companyStatistics(@Param("dto") DateDTO dto);

    /**
     * 查询车队送货统计
     *
     * @param dto
     * @return
     */
    List<MotorcadeStatisticsVO> motorcadeStatistics(@Param("dto") DateDTO dto);

    /**
     * 查询统计数据
     *
     * @param dto
     * @return
     */
    List<QualityStatisticsVO> qualityStatistics(@Param("dto") DateDTO dto);
}
