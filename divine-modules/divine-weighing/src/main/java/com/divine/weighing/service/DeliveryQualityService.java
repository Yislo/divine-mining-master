package com.divine.weighing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.divine.weighing.domain.dto.deliveryquality.QualityUpdateDTO;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.entity.DeliveryQuality;
import com.divine.weighing.domain.vo.deliveryquality.QualityPageVO;
import com.divine.weighing.domain.vo.deliveryquality.QualityStatisticsVO;
import com.divine.weighing.domain.vo.home.CompanyStatisticsVO;
import com.divine.weighing.domain.vo.home.MotorcadeStatisticsVO;
import com.divine.weighing.domain.dto.deliveryquality.QualityPageDTO;

import java.util.List;

/**
 * @Description: 送货质量业务层
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
public interface DeliveryQualityService extends IService<DeliveryQuality> {


    /**
     * 分页查询送货质量
     * @param dto 查询条件
     * @return 查询结果
     */
    IPage<QualityPageVO> queryDeliveryQualityPage(QualityPageDTO dto);

    /**
     * 修改送货质量
     * @param qualityUpdateDTO 送货质量
     * @return 修改结果
     */
    boolean updateDeliveryQualityById(QualityUpdateDTO qualityUpdateDTO);

    /**
     * 查询工厂送货统计
     * @param dto 查询条件
     * @return 修改结果
     */
    List<CompanyStatisticsVO> companyStatistics(DateDTO dto);

    /**
     * 查询统计数据
     * @param dto 查询条件
     * @return 货主送货统计
     */
    List<QualityStatisticsVO> qualityStatistics(DateDTO dto);

    /**
     * 获取车队送货统计
     * @param dto 查询条件
     * @return 货主送货统计
     */
    List<MotorcadeStatisticsVO> motorcadeStatistics(DateDTO dto);
}
