package com.divine.weighing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.exception.base.BusinessException;
import com.divine.weighing.domain.dto.car.CarUpdateDTO;
import com.divine.weighing.domain.entity.Car;
import com.divine.weighing.domain.entity.Delivery;
import com.divine.weighing.domain.vo.car.CarPageVO;
import com.divine.weighing.domain.dto.car.CarPageDTO;
import com.divine.weighing.domain.dto.car.CarSaveBatchDTO;
import com.divine.weighing.domain.vo.car.CarInfoVO;
import com.divine.weighing.domain.vo.car.CarRefuelVO;
import com.divine.weighing.domain.vo.car.CarVO;
import com.divine.weighing.mapper.CarMapper;
import com.divine.weighing.service.CarService;
import com.divine.weighing.service.DeliveryService;
import com.divine.weighing.service.RefuelRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Lazy
    @Autowired
    private CarMapper carMapper;

    @Autowired
    private DeliveryService deliveryService;

    /**
     * 新增车辆信息
     *
     * @param carSaveBatchDTO 车辆信息
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchCar(CarSaveBatchDTO carSaveBatchDTO) {
//        String motorcadeId = carSaveBatchDTO.getMotorcadeId();
//        Motorcade motorcade = motorcadeService.getById(motorcadeId);
//        if (ObjUtil.isNull(motorcade)) {
//            throw new BusinessException("车队不存在");
//        }
//        List<String> carNumberList = carSaveBatchDTO.getCarNumber();
//        // 查询车牌号是否已经存在
//        List<Car> carList = carMapper.selectList(new LambdaQueryWrapper<>(Car.class)
//                .in(Car::getCarNumber, carNumberList));
//        if (CollectionUtil.isNotEmpty(carList)) {
//            List<String> list = carList.stream().map(Car::getCarNumber).toList();
//            throw new BusinessException("以下" + list + "车牌号已存在");
//        }
//        List<Car> catList = carNumberList.stream().map(carNumber -> Car.builder()
//                .carNumber(carNumber)
//                .motorcadeId(carSaveBatchDTO.getMotorcadeId())
//                .build()).toList();
//        return saveBatch(catList);
        return true;
    }

    /**
     * 分页查询车辆信息
     *
     * @param carPageDTO 查询条件
     * @return 查询结果
     */
    @Override
    public IPage<CarPageVO> queryCarPage(CarPageDTO carPageDTO) {
//        // 构建 MP 分页对象（传入当前页、每页条数）
//        IPage<CarPageVO> resultPage = new Page<>(carPageDTO.getPageNum(), carPageDTO.getPageSize());
//        // 调用 Mapper 分页查询（MP 自动拦截，拼接分页 SQL）
//        resultPage = carMapper.queryPageCar(resultPage, carPageDTO);
//        return resultPage;
        return null;
    }

    /**
     * 根据id查询车辆信息
     *
     * @param id 主键id
     * @return 车辆信息
     */
    @Override
    public CarInfoVO getCarById(String id) {
//        Car car = carMapper.selectById(id);
//        LocalDateTime now = LocalDateTime.now();
//        Motorcade motorcade = motorcadeService.getById(car.getMotorcadeId());
//        // 本月开始时间
//        LocalDateTime startOfMonth = now.withDayOfMonth(1).with(LocalTime.MIN);
//        // 本月结束时间
//        LocalDateTime endOfMonth = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear())).with(LocalTime.MAX);
//        // 今年开始时间
//        LocalDateTime startOfYear = now.withDayOfYear(1).with(LocalTime.MIN);
//        // 今年结束时间
//        LocalDateTime endOfYear = now.withDayOfYear(now.getMonth().length(now.toLocalDate().isLeapYear()) * 12).with(LocalTime.MAX);
//        // 本月加油次
//        long refuelMonthNum = refuelRecordService.count(new LambdaQueryWrapper<>(RefuelRecord.class)
//                .between(RefuelRecord::getCreateTime, startOfMonth, endOfMonth));
//        // 今年加油次数
//        long refuelYearNum = refuelRecordService.count(new LambdaQueryWrapper<>(RefuelRecord.class)
//                .between(RefuelRecord::getCreateTime, startOfYear, endOfYear));
//        // 本月送货次数
//        long weighingMonthNum = fileService.count(new LambdaQueryWrapper<>(File.class)
//                .between(File::getCreateTime, startOfMonth, endOfMonth));
//        // 今年送货次数
//        long weighingYearNum = fileService.count(new LambdaQueryWrapper<>(File.class)
//                .between(File::getCreateTime, startOfYear, endOfYear));
//        return CarInfoVO.builder()
//                .id(car.getId())
//                .carNumber(car.getCarNumber())
//                .motorcadeId(car.getMotorcadeId())
//                .motorcadeName(motorcade.getMotorcadeName())
//                .weighingYearNum(weighingYearNum)
//                .weighingMonthNum(weighingMonthNum)
//                .refuelYearNum(refuelYearNum)
//                .refuelMonthNum(refuelMonthNum)
//                .build();
        return null;
    }

    @Override
    public CarVO getCarByCarNum(String carNumber) {
        Car car = carMapper.selectOne(new LambdaQueryWrapper<>(Car.class)
                .eq(Car::getCarNumber, carNumber));
        return BeanUtil.copyProperties(car, CarVO.class);
    }

    /**
     * 查询车辆加油情况
     *
     * @param carNumber 主键id
     * @return
     */
    @Override
    public CarRefuelVO queryRefuel(String carNumber) {
//        Car car = carMapper.selectOne(new LambdaQueryWrapper<>(Car.class)
//                .eq(Car::getCarNumber, carNumber));
//        if (ObjUtil.isNull(car)) {
//            throw new BusinessException("车辆不存在");
//        }
//        // 查询所属车队
//        Motorcade motorcade = motorcadeService.getById(car.getMotorcadeId());
//        // 根据carId查询最近的一条加油记录
//        RefuelRecord refuelRecord = refuelRecordService.getOne(new LambdaQueryWrapper<RefuelRecord>(RefuelRecord.class)
//                .eq(RefuelRecord::getCarNumber, carNumber)
//                .orderByDesc(RefuelRecord::getCreateTime)
//                .last("LIMIT 1"));
//
//        if (ObjUtil.isNotNull(refuelRecord)) {
//            Date createTime = refuelRecord.getCreateTime();
//            // 查询上次加油后送了几次货
//            long count = fileService.count(new LambdaQueryWrapper<File>()
//                    .between(File::getCreateTime, createTime, new Date()));
//            return CarRefuelVO.builder()
//                    .carNumber(carNumber)
//                    .weighingNum(count)
//                    .litre(refuelRecord.getLitre())
//                    .motorcadeName(motorcade.getMotorcadeName())
//                    .refuelTime(refuelRecord.getCreateTime())
//                    .build();
//        }
//        // 如果没有加油 查询所有送货次数
//        long count = fileService.count();
//        return CarRefuelVO.builder()
//                .carNumber(carNumber)
//                .weighingNum(count)
//                .litre(0)
//                .motorcadeName(motorcade.getMotorcadeName())
//                .motorcadeId(motorcade.getId())
//                .refuelTime(null)
//                .build();
        return null;
    }

    /**
     * 修改车辆信息
     *
     * @param carUpdateDTO 车辆信息
     * @return 修改结果
     */
    @Override
    public boolean updateCarById(CarUpdateDTO carUpdateDTO) {
        Car car = BeanUtil.copyProperties(carUpdateDTO, Car.class);
        int updateNum = carMapper.updateById(car);
        return updateNum > 0;
    }

    /**
     * 逻辑删除车辆信息
     *
     * @param id 主键id
     * @return 删除结果
     */
    @Override
    public boolean deleteCarById(String id) {
        Car car = carMapper.selectById(id);
        Delivery one = deliveryService.getOne(new LambdaQueryWrapper<Delivery>(Delivery.class)
                .eq(Delivery::getCarNumber, car.getCarNumber())
                .last("limit 1"));
        if (ObjUtil.isNotNull(one)){
            throw new BusinessException("该车辆有配送信息，暂无法删除");
        }
        int deleteNum = carMapper.deleteById(id);
        return deleteNum > 0;
    }

}
