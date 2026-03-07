package com.divine.system.service;

import com.divine.system.domain.vo.home.*;

import java.util.List;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 15:10
 */
public interface HomeService {

    /**
     * 获取今日出入库
     * @return
     */
    WarehouseInfoVO getWarehouseInfo();

    /**
     * 获取今日进厂数量
     * @return
     */
    AccessRecordInfoVO getAccessRecord();

    /**
     * 获取今日过磅数量
     * @return
     */
    List<WeightingInfoVO> getWeighting();

    /**
     * 获取今日加油量
     * @return
     */
    RefuelInfoVO getRefuel();

    /**
     * 获取近期取样趋势
     * @param type
     * @return
     */
    List<SamplingInfoVO> getSampling(Integer type);

    /**
     * 获取取样客户占比
     * @return
     */
    List<SamplingMerchantVO> getSamplingMerchant();

    /**
     * 获取近7日出库
     * @return
     */
    List<SevenRecordVO> get7DayShipment();

    /**
     * 获取近7日进厂
     * @return
     */
    List<SevenRecordVO> get7DayEntry();

    /**
     * 获取近七日过磅
     * @return
     */
    List<SevenRecordVO> get7dayWeighting();

    /**
     * 获取近七日加油
     * @return
     */
    List<SevenRecordVO> get7dayRefuel();

}
