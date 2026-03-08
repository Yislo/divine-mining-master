package com.divine.system.service;


import com.divine.common.core.domain.vo.OptionVO;
import com.divine.system.domain.vo.SysConfigVo;

import java.util.List;

/**
 * 库存盘点单据详情Service业务层处理
 *
 * @author yisl
 * @date 2024-08-13
 */
public interface CommonService {

    /**
     * 获取下拉框
     * @param type
     * @return
     */
    List<OptionVO> getOption(String type);

    /**
     * 获取配置参数值
     * @param keyName
     * @return
     */
    SysConfigVo getConfigParam(String keyName);
}
