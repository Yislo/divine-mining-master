package com.divine.weighing.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.divine.weighing.domain.dto.refuel.RefuelPageDTO;
import com.divine.weighing.domain.dto.refuel.RefuelRecordDTO;
import com.divine.weighing.domain.dto.refuel.RefuelStatisticsDTO;
import com.divine.weighing.domain.entity.RefuelRecord;
import com.divine.weighing.domain.vo.home.MotorcadeRefuelStatisticsVO;
import com.divine.weighing.domain.vo.refuel.RefuelPageVO;
import com.divine.weighing.domain.vo.refuel.RefuelRecordVO;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.refuel.RefuelSaveDTO;

import java.util.List;

/**
 * @Description: 加油记录业务层
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
public interface RefuelRecordService extends IService<RefuelRecord> {

    /**
     * 新增加油记录
     * @param refuelRecordDTO 加油记录
     * @return 新增结果
     */
    boolean saveRefuelRecord(RefuelSaveDTO refuelRecordDTO);

    /**
     * 分页查询加油记录
     * @param refuelRecordDTO 查询条件
     * @return 查询结果
     */
    IPage<RefuelPageVO> queryRefuelRecordPage(RefuelPageDTO refuelRecordDTO);

    /**
     * 统计加油数据
     * @param dto 主键id
     * @return 加油记录
     */
    RefuelRecordVO refuelStatistics(RefuelStatisticsDTO dto);

    /**
     * 修改加油记录
     * @param refuelRecordDTO 加油记录
     * @return 修改结果
     */
    boolean batchSettlement(RefuelRecordDTO refuelRecordDTO);

    /**
     * 逻辑删除加油记录
     * @param id 主键id
     * @return 删除结果
     */
    boolean deleteRefuelRecordById(String id);

    /**
     * 获取车队加油数据
     * @param dto 查询条件
     * @return 加油数据
     */
    List<MotorcadeRefuelStatisticsVO> motorcadeRefuelStatist(DateDTO dto);
}
