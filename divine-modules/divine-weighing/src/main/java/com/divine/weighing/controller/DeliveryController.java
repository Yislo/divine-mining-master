package com.divine.weighing.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.dto.delivery.*;
import com.divine.weighing.domain.vo.delivery.*;
import com.divine.weighing.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 送货记录Controller
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Tag(name = "送货相关")
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping("/departure")
    @Operation(summary = "发车")
    public Result<Boolean> saveDeparture(@RequestBody @Validated DepartureSaveDTO departureSaveDTO) {
        return Result.success(deliveryService.departure(departureSaveDTO));
    }

    @PostMapping("/weighing")
    @Operation(summary = "过磅")
    public Result<Boolean> weighing(@RequestBody @Validated WeighingDTO dto) {
        return Result.success(deliveryService.weighing(dto));
    }

    @GetMapping("/getBatchCode")
    @Operation(summary = "获取当前批次号")
    public Result<String> getBatchCode(@RequestParam("companyId") String companyId) {
        return Result.success(deliveryService.getBatchCode(companyId));
    }

    @GetMapping("/getNextBatchCode")
    @Operation(summary = "获取下一个批次号")
    public Result<String> getNextBatchCode(@RequestParam("companyId") String companyId) {
        return Result.success(deliveryService.getNextBatchCode(companyId));
    }

    @PostMapping("/returnWeighing")
    @Operation(summary = "回磅")
    public Result<Boolean> returnWeighing(@RequestBody @Validated WeighingWeightDTO dto) {
        return Result.success(deliveryService.returnWeighing(dto));
    }

    @PostMapping("/saveSampleFile")
    @Operation(summary = "保存取样文件")
    public Result<Boolean> saveSampleFile(@RequestBody @Validated SaveSampleFileDTO dto) {
        return Result.success(deliveryService.saveSampleFile(dto));
    }

    @PostMapping("/deliveryCarPage")
    @Operation(summary = "分页查询送货记录")
    public Result<IPage<DeliveryCarPageVO>> deliveryCarPage(@RequestBody @Validated DeparturePageDTO departureDTO) {
        return Result.success(deliveryService.deliveryCarPage(departureDTO));
    }

    @PostMapping("/deliveryPatchPage")
    @Operation(summary = "分页查询取样批次")
    public Result<IPage<DeliveryBatchPageVO>> deliveryPatchPage(@RequestBody @Validated DeparturePageDTO departureDTO) {
        return Result.success(deliveryService.deliveryBatchPage(departureDTO));
    }


    @GetMapping("/getDeliveryInfo/{id}")
    @Operation(summary = "送货记录详情")
    public Result<DeliveryCarInfoVO> getDeliveryInfo(@PathVariable("id") String id) {
        return Result.success(deliveryService.getDeliveryInfo(id));
    }


    @GetMapping("/getDeliveryNum")
    @Operation(summary = "获取送货数量")
    public Result<DeliveryNumVO> getDeliveryNum() {
        return Result.success(deliveryService.getDeliveryNum());
    }


    @GetMapping("/getBatchInfo/{batchCode}")
    @Operation(summary = "批次取样详情")
    public Result<DeliveryBatchInfoVO> getBatchInfo(@PathVariable("batchCode") String batchCode) {
        return Result.success(deliveryService.getBatchInfo(batchCode));
    }


    @PutMapping("/updateDelivery")
    @Operation(summary = "修改送货记录")
    public Result<Boolean> updateDelivery(@RequestBody @Validated DeliveryUpdateDTO deliveryUpdateDTO) {
        return Result.success(deliveryService.updateDeliveryById(deliveryUpdateDTO));
    }

    @DeleteMapping("/deleteDelivery")
    @Operation(summary = "删除送货记录")
    public Result<Boolean> deleteDeliveryById(@RequestParam("id") String id) {
        return Result.success(deliveryService.deleteDeliveryById(id));
    }
}
