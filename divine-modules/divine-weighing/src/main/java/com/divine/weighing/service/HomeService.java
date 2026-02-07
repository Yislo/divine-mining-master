package com.divine.weighing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.home.UnfinishedDTO;
import com.divine.weighing.domain.vo.home.*;

import java.util.List;

/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
public interface HomeService {


    /**
     * 后管-数据总计
     * @return
     */
    StatisticsVO statistics();

    /**
     * 后管&app -未到车辆
     * @return
     */
    IPage<UnfinishedVO> unfinished(UnfinishedDTO dto);

    /**
     * 后管&App-管理员统计图
     * @param dateDTO
     * @return
     */
    List<CartogramInfoVO> cartogram(DateDTO dateDTO);

    /**
     * App-统计图
     * @return
     */
    PieChartVO pieChart(DateDTO dateDTO);

    /**
     * App-加油统计
     * @return
     */
    RefuelStatisticsVO refuelStatistics();

    /**
     * 后管-工厂送货统计
     * @param dto
     * @return
     */
    List<CompanyStatisticsVO> companyStatistics(DateDTO dto);

    /**
     * 后管-矿主送货统计
     * @param dto
     * @return
     */
    List<OwnerStatisticsVO> ownerStatistics(DateDTO dto);

    /**
     * 后管-车队送货统计
     * @param dto
     * @return
     */
    List<MotorcadeStatisticsVO> motorcadeStatistics(DateDTO dto);

    /**
     * 后管-车队加油统计
     * @param dto
     * @return
     */
    List<MotorcadeRefuelStatisticsVO> motorRefuelStatist(DateDTO dto);

    /**
     * App-近期活动
     * @return
     */
    RecentVO recent();
}
