package com.divine.weighing.service.impl;

import com.divine.common.core.service.UserService;
import com.divine.weighing.domain.vo.OptionVO;
import com.divine.weighing.service.CarService;
import com.divine.weighing.service.OptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Service
public class OptionsServiceImpl implements OptionsService {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;


    public List<OptionVO> getOptionsByType(String type) {

        switch (type) {
//            case "deliverStatus":
//                return DeliverStatusEnum.toOptions();
//
//            case "refuelStatus":
//                return RefuelStatusEnum.toOptions();
//
//            case "deliveryExportField":
//                return DeliveryExportFieldEnum.toOptions();
//
//            case "roleType":
//                return roleService.list().stream()
//                        .map(o -> new OptionVO(o.getRoleName(), o.getRoleCode()))
//                        .collect(Collectors.toList());
//
//            case "owner":
//                return ownerService.list().stream()
//                        .map(o -> new OptionVO(o.getName(), o.getId()))
//                        .collect(Collectors.toList());
//
//            case "company":
//                return companyService.list().stream()
//                        .map(c -> new OptionVO(c.getCompanyName(), c.getId()))
//                        .collect(Collectors.toList());
//
//            case "car":
//                return carService.list().stream()
//                        .map(c -> new OptionVO(c.getCarNumber(), c.getCarNumber()))
//                        .collect(Collectors.toList());
//
//            case "motorcade":
//                return motorcadeService.list().stream()
//                        .map(m -> new OptionVO(m.getMotorcadeName(), m.getId()))
//                        .collect(Collectors.toList());
//
//            case "user":
//                return userService.list().stream()
//                        .map(m -> new OptionVO(m.getNickName(), m.getId()))
//                        .collect(Collectors.toList());

            default:
                throw new IllegalArgumentException("不支持的下拉类型: " + type);
        }
    }
}
