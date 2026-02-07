package com.divine.weighing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.dto.car.CarPageDTO;
import com.divine.weighing.domain.dto.car.CarUpdateDTO;
import com.divine.weighing.domain.vo.car.CarPageVO;
import com.divine.weighing.domain.dto.car.CarSaveBatchDTO;
import com.divine.weighing.domain.vo.car.CarInfoVO;
import com.divine.weighing.domain.vo.car.CarRefuelVO;
import com.divine.weighing.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 车辆信息Controller
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Tag(name = "车辆信息相关")
@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/saveBatchCar")
    @Operation(summary = "批量新增车辆")
    public Result<Boolean> saveBatchCar(@RequestBody @Validated CarSaveBatchDTO carSaveBatchDTO) {
        return Result.success(carService.saveBatchCar(carSaveBatchDTO));
    }

    @PostMapping("/queryCarPage")
    @Operation(summary = "分页查询车辆信息")
    public Result<IPage<CarPageVO>> queryCarPage(@RequestBody @Validated CarPageDTO carPageDTO) {
        return Result.success(carService.queryCarPage(carPageDTO));
    }

    @GetMapping("/getCarById")
    @Operation(summary = "查询车辆信息")
    public Result<CarInfoVO> getCarById(@RequestParam("id") String id) {
        return Result.success(carService.getCarById(id));
    }

    @GetMapping("/queryRefuel")
    @Operation(summary = "查询车辆加油情况")
    public Result<CarRefuelVO> updateCar(@RequestParam("carNumber") String carNumber) {
        return Result.success(carService.queryRefuel(carNumber));
    }

    @PutMapping("/updateCar")
    @Operation(summary = "修改车辆信息")
    public Result<Boolean> updateCar(@RequestBody @Validated CarUpdateDTO carUpdateDTO) {
        return Result.success(carService.updateCarById(carUpdateDTO));
    }

    @DeleteMapping("/deleteCar")
    @Operation(summary = "删除车辆")
    public Result<Boolean> deleteCarById(@RequestParam("id") String id) {
        return Result.success(carService.deleteCarById(id));
    }
}
