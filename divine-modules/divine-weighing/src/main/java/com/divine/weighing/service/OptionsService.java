package com.divine.weighing.service;



import com.divine.weighing.domain.vo.OptionVO;

import java.util.List;

/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
public interface OptionsService {



    List<OptionVO> getOptionsByType(String type);
}
