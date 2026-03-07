package com.divine.system.service.impl;

import com.divine.system.domain.vo.home.*;
import com.divine.system.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 15:10
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    /**
     * 获取今日出入库
     *
     * @return
     */
    @Override
    public WarehouseInfoVO getWarehouseInfo() {
        return null;
    }

    /**
     * 获取今日进厂数量
     *
     * @return
     */
    @Override
    public AccessRecordInfoVO getAccessRecord() {
        return null;
    }

    /**
     * 获取今日过磅数量
     *
     * @return
     */
    @Override
    public List<WeightingInfoVO> getWeighting() {
        return List.of();
    }

    /**
     * 获取今日加油量
     *
     * @return
     */
    @Override
    public RefuelInfoVO getRefuel() {
        return null;
    }

    /**
     * 获取近期取样趋势
     *
     * @param type
     * @return
     */
    @Override
    public List<SamplingInfoVO> getSampling(Integer type) {
        return List.of();
    }

    /**
     * 获取取样客户占比
     *
     * @return
     */
    @Override
    public List<SamplingMerchantVO> getSamplingMerchant() {
        return List.of();
    }

    /**
     * 获取近7日出库
     *
     * @return
     */
    @Override
    public List<SevenRecordVO> get7DayShipment() {
        return List.of();
    }

    /**
     * 获取近7日进厂
     *
     * @return
     */
    @Override
    public List<SevenRecordVO> get7DayEntry() {
        return List.of();
    }

    /**
     * 获取近七日过磅
     *
     * @return
     */
    @Override
    public List<SevenRecordVO> get7dayWeighting() {
        return List.of();
    }

    /**
     * 获取近七日加油
     *
     * @return
     */
    @Override
    public List<SevenRecordVO> get7dayRefuel() {
        return List.of();
    }
}
