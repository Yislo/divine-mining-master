package com.divine.weighing.service.impl;

import com.baomidou.lock.LockTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.divine.common.core.service.UserService;
import com.divine.weighing.domain.dto.deliveryquality.QualityUpdateDTO;
import com.divine.weighing.domain.entity.DeliveryQuality;
import com.divine.weighing.domain.dto.deliveryquality.QualityPageDTO;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.vo.deliveryquality.QualityPageVO;
import com.divine.weighing.domain.vo.deliveryquality.QualityStatisticsVO;
import com.divine.weighing.domain.vo.home.CompanyStatisticsVO;
import com.divine.weighing.domain.vo.home.MotorcadeStatisticsVO;
import com.divine.weighing.mapper.DeliveryQualityMapper;
import com.divine.weighing.service.DeliveryQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 送货质量业务层
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Service
public class DeliveryQualityServiceImpl extends ServiceImpl<DeliveryQualityMapper, DeliveryQuality> implements DeliveryQualityService {

    @Autowired
    private DeliveryQualityMapper deliveryQualityMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LockTemplate lockTemplate;


    /**
     * 分页查询送货质量
     *
     * @param dto 查询条件
     * @return 查询结果
     */
    @Override
    public IPage<QualityPageVO> queryDeliveryQualityPage(QualityPageDTO dto) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        IPage<QualityPageVO> resultPage = new Page<>(dto.getPageNum(), dto.getPageSize());
//        resultPage = baseMapper.queryQualityPage(resultPage, dto);
//        // 计算金吨
//        resultPage.getRecords().forEach(qualityPageVO -> {
//            // 计算金吨 金吨=净重*（100-水）/100*氧化铜/100
//            Integer netWeight = qualityPageVO.getNetWeight();
//            String moisture = qualityPageVO.getMoisture();
//            String cuoRatio = qualityPageVO.getCuoRatio();
//            qualityPageVO.setMoisture(moisture);
//            qualityPageVO.setCuoRatio(cuoRatio);
//            if (ObjUtil.isNotNull(netWeight)
//                    && StringUtils.isNotBlank(moisture)
//                    && StringUtils.isNotBlank(cuoRatio)
//                    && RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode())) {
//                BigDecimal goldTon = new BigDecimal(netWeight)
//                        .multiply(BigDecimal.valueOf(100).subtract(new BigDecimal(moisture)))
//                        .multiply(new BigDecimal(cuoRatio))
//                        .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
//                qualityPageVO.setGoldTon(goldTon.toString());
//            } else {
//                qualityPageVO.setGoldTon(Constant.DEFAULT_EMPTY_DATA);
//            }
//
//
//        });
//        return resultPage;
        return null;
    }

    /**
     * 修改送货质量
     *
     * @param dto 送货质量
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeliveryQualityById(QualityUpdateDTO dto) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        boolean adminOrLeader = RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode());
//        if (!adminOrLeader) {
//            throw new BusinessException("无权限修改");
//        }
//        String lockKey = dto.getId();
//        String requestId = UUID.randomUUID().toString();
//        boolean locked = redisDistributedLock.lock(lockKey, requestId, 10, 3);
//        if (!locked) {
//            throw new BusinessException("系统繁忙，请稍后再试");
//        }
//        try {
//            DeliveryQuality deliveryQuality = BeanUtil.copyProperties(dto, DeliveryQuality.class);
//            deliveryQualityMapper.update(deliveryQuality,new LambdaQueryWrapper<>(DeliveryQuality.class)
//                    .eq(DeliveryQuality::getDeliveryId, deliveryQuality.getId()));
//        } finally {
//            redisDistributedLock.unlock(lockKey, requestId);
//        }
        return true;
    }

    /**
     * 查询工厂送货统计
     *
     * @param dto 送货质量
     * @return 统计结果
     */
    @Override
    public List<CompanyStatisticsVO> companyStatistics(DateDTO dto) {
        return deliveryQualityMapper.companyStatistics(dto);
    }

    /**
     * 查询统计数据
     *
     * @param dto 货主id
     * @return 统计结果
     */
    @Override
    public List<QualityStatisticsVO> qualityStatistics(DateDTO dto) {
        return deliveryQualityMapper.qualityStatistics(dto);
    }

    /**
     * 查询车队送货统计
     *
     * @param dto 货主id
     * @return
     */
    @Override
    public List<MotorcadeStatisticsVO> motorcadeStatistics(DateDTO dto) {
        return deliveryQualityMapper.motorcadeStatistics(dto);
    }

}
