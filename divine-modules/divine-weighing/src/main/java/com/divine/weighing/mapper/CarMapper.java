package com.divine.weighing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.weighing.domain.dto.car.CarPageDTO;
import com.divine.weighing.domain.entity.Car;
import com.divine.weighing.domain.vo.car.CarPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 车辆信息持久层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {
    /**
     * 分页查询车辆信息
     * @param Page
     * @param dto
     * @return
     */
    IPage<CarPageVO> queryPageCar(IPage<CarPageVO> Page , @Param("dto") CarPageDTO dto);

}
