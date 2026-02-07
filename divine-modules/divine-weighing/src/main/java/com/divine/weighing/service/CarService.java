package com.divine.weighing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.divine.weighing.domain.dto.car.CarPageDTO;
import com.divine.weighing.domain.dto.car.CarUpdateDTO;
import com.divine.weighing.domain.entity.Car;
import com.divine.weighing.domain.vo.car.CarPageVO;
import com.divine.weighing.domain.vo.car.CarVO;
import com.divine.weighing.domain.dto.car.CarSaveBatchDTO;
import com.divine.weighing.domain.vo.car.CarInfoVO;
import com.divine.weighing.domain.vo.car.CarRefuelVO;

/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
public interface CarService extends IService<Car> {

    /**
     * 新增车辆信息
     * @param carSaveBatchDTO 车辆信息
     * @return 新增结果
     */
    boolean saveBatchCar(CarSaveBatchDTO carSaveBatchDTO);

    /**
     * 分页查询车辆信息
     * @param carPageDTO 查询条件
     * @return 查询结果
     */
    IPage<CarPageVO> queryCarPage(CarPageDTO carPageDTO);

    /**
     * 根据id查询车辆信息
     * @param id 主键id
     * @return 车辆信息
     */
    CarInfoVO getCarById(String id);

    /**
     * 根据车牌号查询车辆信息
     * @param id 主键id
     * @return 车辆信息
     */
    CarVO getCarByCarNum(String id);

    /**
     * 查询车辆加油情况
     * @param carNumber 主键id
     * @return 车辆信息
     */
    CarRefuelVO queryRefuel(String carNumber);

    /**
     * 修改车辆信息
     * @param carUpdateDTO 车辆信息
     * @return 修改结果
     */
    boolean updateCarById(CarUpdateDTO carUpdateDTO);

    /**
     * 逻辑删除车辆信息
     * @param id 主键id
     * @return 删除结果
     */
    boolean deleteCarById(String id);

}
