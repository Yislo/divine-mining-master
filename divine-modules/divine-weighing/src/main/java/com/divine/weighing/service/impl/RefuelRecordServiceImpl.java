package com.divine.weighing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.weighing.domain.dto.refuel.RefuelPageDTO;
import com.divine.weighing.domain.dto.refuel.RefuelStatisticsDTO;
import com.divine.weighing.domain.entity.RefuelRecord;
import com.divine.weighing.domain.vo.home.MotorcadeRefuelStatisticsVO;
import com.divine.weighing.domain.vo.refuel.RefuelPageVO;
import com.divine.weighing.domain.vo.refuel.RefuelRecordVO;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.refuel.RefuelRecordDTO;
import com.divine.weighing.domain.dto.refuel.RefuelSaveDTO;
import com.divine.weighing.mapper.RefuelRecordMapper;
import com.divine.weighing.service.CarService;
import com.divine.weighing.service.RefuelRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 加油记录业务层
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Service
public class RefuelRecordServiceImpl extends ServiceImpl<RefuelRecordMapper, RefuelRecord> implements RefuelRecordService {

    @Autowired
    private RefuelRecordMapper refuelRecordMapper;


    /**
     * 新增加油记录
     *
     * @param refuelRecordDTO 加油记录
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRefuelRecord(RefuelSaveDTO refuelRecordDTO) {
//        // 校验车牌号是否存在
//        Car one = carService.getOne(new LambdaQueryWrapper<>(Car.class).eq(Car::getCarNumber, refuelRecordDTO.getCarNumber()));
//        if (ObjUtil.isNull(one)) {
//            throw new BusinessException("车牌号有误");
//        }
//        RefuelRecord refuelRecord = BeanUtil.copyProperties(refuelRecordDTO, RefuelRecord.class);
//        refuelRecord.setStatus(0);
//        int saveNum = refuelRecordMapper.insert(refuelRecord);
//        String filePath = refuelRecordDTO.getFilePath();
//        if (StringUtils.isNotBlank(filePath)) {
//            fileService.batchSaveFile(FileBatchSaveDTO.builder()
//                    .fileList(List.of(filePath))
//                    .businessType(BusinessTypeEnum.REFUEL.getValue())
//                    .businessId(refuelRecord.getId())
//                    .build());
//        }
//        return saveNum > 0;
        return true;
    }

    /**
     * 分页查询加油记录
     *
     * @param refuelRecordDTO 查询条件
     * @return 查询结果
     */
    @Override
    public IPage<RefuelPageVO> queryRefuelRecordPage(RefuelPageDTO refuelRecordDTO) {
//        IPage<RefuelPageVO> resPage = new Page<>(refuelRecordDTO.getPageNum(), refuelRecordDTO.getPageSize());
//        resPage = refuelRecordMapper.queryRefuelPage(resPage, refuelRecordDTO);
//        return resPage;
        return null;
    }


    /**
     * 加油数据统计
     *
     * @param dto 主键id
     * @return
     */
    @Override
    public RefuelRecordVO refuelStatistics(RefuelStatisticsDTO dto) {
//        refuelRecordMapper.selectCount(new LambdaQueryWrapper<>()
//                .eq(StringUtils.isNotBlank(dto.getCompanyId(),RefuelRecord::getMotorcadeId))
//        );
//
        return null;
    }

    /**
     * 修改加油记录
     *
     * @param refuelRecordDTO 加油记录
     * @return 修改结果
     */
    @Override
    public boolean batchSettlement(RefuelRecordDTO refuelRecordDTO) {
        RefuelRecord refuelRecord = BeanUtil.copyProperties(refuelRecordDTO, RefuelRecord.class);
        int updateNum = refuelRecordMapper.updateById(refuelRecord);
        return updateNum > 0;
    }

    /**
     * 逻辑删除加油记录
     *
     * @param id 主键id
     * @return 删除结果
     */
    @Override
    public boolean deleteRefuelRecordById(String id) {
        int deleteNum = refuelRecordMapper.deleteById(id);
        return deleteNum > 0;
    }

    /**
     * 车队加油数据统计
     *
     * @param dto
     * @return
     */
    @Override
    public List<MotorcadeRefuelStatisticsVO> motorcadeRefuelStatist(DateDTO dto) {
        return refuelRecordMapper.motorcadeRefuelStatist(dto);
    }

}
