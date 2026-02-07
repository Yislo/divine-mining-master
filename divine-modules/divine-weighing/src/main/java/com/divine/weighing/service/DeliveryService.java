package com.divine.weighing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.divine.weighing.domain.dto.delivery.*;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.entity.Delivery;
import com.divine.weighing.domain.vo.delivery.*;
import com.divine.weighing.domain.vo.home.CartogramInfoVO;
import com.divine.weighing.domain.vo.home.PieChartVO;
import com.divine.weighing.export.DeliveryExportField;

import java.util.List;

/**
 * @Description: 送货记录业务层
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
public interface DeliveryService extends IService<Delivery> {

    /**
     * 新增发车记录
     *
     * @param departureSaveDTO 发车记录
     * @return 新增结果
     */
    boolean departure(DepartureSaveDTO departureSaveDTO);

    /**
     * 过磅
     *
     * @param dto 发车记录
     * @return 新增结果
     */
    boolean weighing(WeighingDTO dto);

    /**
     * 回磅
     *
     * @param dto
     * @return 新增结果
     */
    boolean returnWeighing(WeighingWeightDTO dto);

    /**
     * 保存取样文件
     *
     * @param dto
     * @return 新增结果
     */
    boolean saveSampleFile(SaveSampleFileDTO dto);

    /**
     * 获取当前批次号
     *
     * @return 批次号
     */
    String getBatchCode(String companyId);

    /**
     * 获取下一个批次号
     *
     * @return 批次号
     */
    String getNextBatchCode(String companyId);

    /**
     * 分页查询发车记录(按车辆)
     *
     * @param departureDTO 查询条件
     * @return 查询结果
     */
    IPage<DeliveryCarPageVO> deliveryCarPage(DeparturePageDTO departureDTO);

    /**
     * 分页查询发车记录(按批次)-功能弃置
     *
     * @param departureDTO 查询条件
     * @return 查询结果
     */
    IPage<DeliveryBatchPageVO> deliveryBatchPage(DeparturePageDTO departureDTO);

    /**
     * 统计所有工厂发车记录
     *
     * @param dateDTO 时间
     * @return 发车记录
     */
    List<CartogramInfoVO> cartogram(DateDTO dateDTO);

    /**
     * 统计单个工厂发车记录
     *
     * @param dateDTO 时间
     * @return 发车记录
     */
    PieChartVO pieChart(DateDTO dateDTO, String companyId);

    /**
     * 获取送货详情
     *
     * @param id
     * @return
     */
    DeliveryCarInfoVO getDeliveryInfo(String id);

    /**
     * 查询批次详情 - 功能弃置
     *
     * @param batchCode
     * @return
     */
    DeliveryBatchInfoVO getBatchInfo(String batchCode);

    /**
     * 修改送货记录
     *
     * @param deliveryUpdateDTO 送货记录
     * @return 修改结果
     */
    boolean updateDeliveryById(DeliveryUpdateDTO deliveryUpdateDTO);

    /**
     * 逻辑删除送货记录
     *
     * @param id 主键id
     * @return 删除结果
     */
    boolean deleteDeliveryById(String id);

    /**
     * 查询导出数据
     *
     * @param departureDTO 查询条件
     * @return 查询结果
     */
    IPage<DeliveryExportField> selectExportList(DeparturePageDTO departureDTO);

    /**
     * 获取发车数量
     * @return
     */
    DeliveryNumVO getDeliveryNum();

}
