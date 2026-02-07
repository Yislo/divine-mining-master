package com.divine.weighing.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.vo.home.*;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.home.UnfinishedDTO;
import com.divine.weighing.service.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lus
 * @Description:
 * @Date: 2025/11/6 13:40
 */
@Tag(name = "首页相关")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @PostMapping("/statistics")
    @Operation(summary = "后管-数据总计")
    public Result<StatisticsVO> statistics(@RequestBody @Validated DateDTO dateDTO) {
        return Result.success(homeService.statistics());
    }

    @PostMapping("/unfinished")
    @Operation(summary = "后管&App-未到车辆")
    public Result<IPage<UnfinishedVO>> unfinished(@RequestBody @Validated UnfinishedDTO dto) {
        return Result.success(homeService.unfinished(dto));
    }

    @PostMapping("/cartogram")
    @Operation(summary = "后管&App-管理员统计图")
    public Result<List<CartogramInfoVO>> cartogram(@RequestBody @Validated DateDTO dateDTO) {
        return Result.success(homeService.cartogram(dateDTO));
    }

    @PostMapping("/pieChart")
    @Operation(summary = "App-普通用户统计图")
    public Result<PieChartVO> pieChart(@RequestBody @Validated DateDTO dateDTO) {
        return Result.success(homeService.pieChart(dateDTO));
    }

    @PostMapping("/refuelStatistics")
    @Operation(summary = "App-加油统计")
    public Result<RefuelStatisticsVO> home(@RequestBody @Validated DateDTO dateDTO) {
        return Result.success(homeService.refuelStatistics());
    }

    @PostMapping("/companyStatistics")
    @Operation(summary = "后管-工厂送货统计")
    public Result<List<CompanyStatisticsVO>> companyStatistics(@RequestBody @Validated DateDTO dto) {
        return Result.success(homeService.companyStatistics(dto));
    }

    @PostMapping("/ownerStatistics")
    @Operation(summary = "后管-矿主送货统计")
    public Result<List<OwnerStatisticsVO>> ownerStatistics(@RequestBody @Validated DateDTO dto) {
        return Result.success(homeService.ownerStatistics(dto));
    }

    @PostMapping("/motorcadeStatistics")
    @Operation(summary = "后管-车队送货统计")
    public Result<List<MotorcadeStatisticsVO>> motorcadeStatistics(@RequestBody @Validated DateDTO dto) {
        return Result.success(homeService.motorcadeStatistics(dto));
    }

    @PostMapping("/motorRefuelStatist")
    @Operation(summary = "后管-车队加油统计")
    public Result<List<MotorcadeRefuelStatisticsVO>> motorRefuelStatist(@RequestBody @Validated DateDTO dto) {
        return Result.success(homeService.motorRefuelStatist(dto));
    }

    @PostMapping("/recent")
    @Operation(summary = "后管&App-近期活动")
    public Result<RecentVO> recent() {
        return Result.success(homeService.recent());
    }
}
