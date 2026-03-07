package com.divine.system.controller.system;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.divine.common.core.domain.Result;
import com.divine.system.domain.vo.home.*;
import com.divine.system.service.HomeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 首页and数据大屏
 *
 * @Author: Yisl
 * @Description:
 * @Date: 2026/3/7 14:09
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class SysHomeController {

    private final HomeService homeService;

    /**
     * 获取今日出入库
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getWarehouseInfo")
    public Result<WarehouseInfoVO> getWarehouseInfo() {
        return Result.success(homeService.getWarehouseInfo());
    }

    /**
     * 获取今日进厂数量
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getAccessRecord")
    public Result<AccessRecordInfoVO> getAccessRecord() {
        return Result.success(homeService.getAccessRecord());
    }

    /**
     * 获取今日过磅数量
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getWeighting")
    public Result<List<WeightingInfoVO>> getWeighting() {
        return Result.success(homeService.getWeighting());
    }

    /**
     * 获取今日加油量
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getRefuel")
    public Result<RefuelInfoVO> getRefuel() {
        return Result.success(homeService.getRefuel());
    }

    /**
     * 获取近期取样趋势
     * @param type 查询维度(0:按月，1:按年)
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getSampling")
    public Result<List<SamplingInfoVO>> getSampling(@NotNull(message = "查询维度不能为空") Integer type) {
        return Result.success(homeService.getSampling(type));
    }

    /**
     * 获取取样客户占比
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/getSamplingMerchant")
    public Result<List<SamplingMerchantVO>> getSamplingMerchant() {
        return Result.success(homeService.getSamplingMerchant());
    }

    /**
     * 近七日出库
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/get7dayShipment")
    public Result<List<SevenRecordVO>> get7DayShipment() {
        return Result.success(homeService.get7DayShipment());
    }

    /**
     * 近七日进厂
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/get7dayEntry")
    public Result<List<SevenRecordVO>> get7DayEntry() {
        return Result.success(homeService.get7DayEntry());
    }

    /**
     * 近七日过磅
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/get7dayWeighting")
    public Result<List<SevenRecordVO>> get7dayWeighting() {
        return Result.success(homeService.get7dayWeighting());
    }

    /**
     * 近七日加油
     * @return
     */
    @SaCheckPermission("system:home:all")
    @GetMapping("/get7dayRefuel")
    public Result<List<SevenRecordVO>> get7dayRefuel() {
        return Result.success(homeService.get7dayRefuel());
    }





}
