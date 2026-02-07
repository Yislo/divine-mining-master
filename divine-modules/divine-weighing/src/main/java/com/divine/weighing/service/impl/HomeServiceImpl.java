package com.divine.weighing.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.service.UserService;
import com.divine.weighing.domain.vo.home.*;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.dto.home.UnfinishedDTO;
import com.divine.weighing.service.DeliveryQualityService;
import com.divine.weighing.service.DeliveryService;
import com.divine.weighing.service.HomeService;
import com.divine.weighing.service.RefuelRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * @Description: 车辆信息业务层
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserService userService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryQualityService deliveryQualityService;

    @Autowired
    private RefuelRecordService refuelRecordService;


    /**
     * 后管-数据总计
     *
     * @return
     */
    @Override
    public StatisticsVO statistics() {
        return StatisticsVO.builder()
                .companyNum(6)
                .deliverGoodsNum(6554)
                .totalProceeds(new BigDecimal("31234"))
                .totalProceedsNum(31)
                .delayProceeds(new BigDecimal("63422"))
                .delayProceedsNum(21)
                .build();
    }

    /**
     * 后管&app-未到车辆
     *
     * @return
     */
    @Override
    public IPage<UnfinishedVO> unfinished(UnfinishedDTO dto) {
////        LoginUserVO userInfo = userUtils.getLoginUserInfo();
////        Integer type = userInfo.getType();
//        DeparturePageDTO departurePageDTO = BeanUtil.copyProperties(dto, DeparturePageDTO.class);
////        // 如果不是管理员用户 就只能查询所属工厂的数据
////        if (!ObjUtil.equals(type, UserTypeEnum.LEADER.getCode()) || !ObjUtil.equals(type, UserTypeEnum.ROOT.getCode())) {
////            departurePageDTO.setCompanyId(userInfo.getCompanyId());
////            departurePageDTO.setStatus(dto.getDeliverStatus());
////        }else {
////            departurePageDTO.setStatus(dto.getDeliverStatus());
////        }
//        departurePageDTO.setStatus(DeliverStatusEnum.DEPART.getCode());
//        IPage<DeliveryCarPageVO> departurePageVOPage = deliveryService.deliveryCarPage(departurePageDTO);
//        IPage<UnfinishedVO> res = departurePageVOPage.convert(deliveryCarPageVO ->
//                BeanUtil.copyProperties(deliveryCarPageVO, UnfinishedVO.class));
//        res.getRecords().forEach(u -> {
//            String remark = u.getRemark();
//            if (StringUtils.isNotBlank(remark)) {
//                // 去除[]
//                u.setRemark(remark.substring(1, remark.length() - 1));
//            }
//        });
//        return res;
        return null;
    }


    /**
     * 后管&App-管理员统计图
     *
     * @param dateDTO
     * @return
     */
    @Override
    public List<CartogramInfoVO> cartogram(DateDTO dateDTO) {
//        List<CartogramInfoVO> cartogram = deliveryService.cartogram(dateDTO);
//        UserInfoVO userInfo = userService.getUserInfo();
//        // 不是管理员 只查自己的
//        if (!RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode())) {
//            cartogram = cartogram.stream().filter(c -> c.getCompanyId().equals(userInfo.getCompanyId())).toList();
//        }
//        return cartogram;
        return null;
    }

    /**
     * App-普通用户统计图
     *
     * @return
     */
    @Override
    public PieChartVO pieChart(DateDTO dateDTO) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        String companyId = userInfo.getCompanyId();
//        return deliveryService.pieChart(dateDTO, companyId);
        return null;
    }

    /**
     * App-加油统计
     *
     * @return
     */
    @Override
    public RefuelStatisticsVO refuelStatistics() {
        return RefuelStatisticsVO.builder()
                .todayRefuelCar(5)
                .todayRefuel(500)
                .monthRefuelCar(65)
                .monthRefuel(5200)
                .build();
    }

    /**
     * 后管-工厂送货统计
     *
     * @param dto
     * @return
     */
    @Override
    public List<CompanyStatisticsVO> companyStatistics(DateDTO dto) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        List<QualityStatisticsVO> qsList = deliveryQualityService.qualityStatistics(dto);
//        Map<String, List<QualityStatisticsVO>> companyMap = qsList.stream().collect(Collectors.groupingBy(QualityStatisticsVO::getCompanyId));
//        // 遍历
//        return companyMap.values().stream().map(companyList -> {
//            CompanyStatisticsVO res = new CompanyStatisticsVO();
//            res.setCompanyName(companyList.get(0).getCompanyName());
//            res.setDeliveryNum(companyList.size());
//
//            if (RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode())) {
//                // 总金吨
//                BigDecimal totalGoldTon = BigDecimal.ZERO;
//                // 总干重
//                BigDecimal totalWeight = BigDecimal.ZERO;
//                for (QualityStatisticsVO qs : companyList) {
//                    Integer netWeight = qs.getNetWeight();
//                    BigDecimal moisture = qs.getMoisture();
//                    BigDecimal cuoRatio = qs.getCuoRatio();
//                    // 计算金吨 金吨=净重*（100-水）/100*氧化铜/100
//                    if (ObjUtil.isNotNull(netWeight)
//                            && ObjUtil.isNotNull(moisture)
//                            && ObjUtil.isNotNull(cuoRatio)) {//总干重=总重-水份
//                        totalWeight = totalWeight.add(
//                                // 总净重
//                                new BigDecimal(netWeight)
//                                        //水份
//                                        .subtract(moisture.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
//                        // 总金吨
//                        totalGoldTon = totalGoldTon.add(new BigDecimal(netWeight)
//                                .multiply(BigDecimal.valueOf(100).subtract(moisture))
//                                .multiply(cuoRatio)
//                                .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
//                    }
//                }
//                // 计算平均品位 总金吨/总干重
//                if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
//                    res.setAverageRatio("0.00");
//                } else {
//                    res.setAverageRatio(totalGoldTon.divide(totalWeight, 2, RoundingMode.HALF_UP).toString());
//                }
//            }
//            return res;
//        }).toList();
        return null;
    }

    /**
     * 后管-矿主送货统计
     *
     * @param dto
     * @return
     */
    @Override
    public List<OwnerStatisticsVO> ownerStatistics(DateDTO dto) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        List<QualityStatisticsVO> qsList = deliveryQualityService.qualityStatistics(dto);
//        Map<String, List<QualityStatisticsVO>> ownerMap = qsList.stream().collect(Collectors.groupingBy(QualityStatisticsVO::getOwnerId));
//        // 遍历
//        return ownerMap.values().stream().map(ownerList -> {
//            OwnerStatisticsVO res = new OwnerStatisticsVO();
//            res.setOwnerName(ownerList.get(0).getOwnerName());
//            res.setDeliveryNum(ownerList.size());
//
//            if (RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode())) {
//                // 总金吨
//                BigDecimal totalGoldTon = BigDecimal.ZERO;
//                // 总干重
//                BigDecimal totalWeight = BigDecimal.ZERO;
//                for (QualityStatisticsVO qs : ownerList) {
//                    Integer netWeight = qs.getNetWeight();
//                    BigDecimal moisture = qs.getMoisture();
//                    BigDecimal cuoRatio = qs.getCuoRatio();
//                    // 计算金吨 金吨=净重*（100-水）/100*氧化铜/100
//                    if (ObjUtil.isNotNull(netWeight)
//                            && ObjUtil.isNotNull(moisture)
//                            && ObjUtil.isNotNull(cuoRatio)) {
//                        //总干重=总重-水份
//                        totalWeight = totalWeight.add(
//                                // 总净重
//                                new BigDecimal(netWeight)
//                                        //水份
//                                        .subtract(moisture.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
//                        // 总金吨
//                        totalGoldTon = totalGoldTon.add(new BigDecimal(netWeight)
//                                .multiply(BigDecimal.valueOf(100).subtract(moisture))
//                                .multiply(cuoRatio)
//                                .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
//                    }
//                }
//                // 计算平均品位 总金吨/总干重
//                if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
//                    res.setAverageRatio("0.00");
//                } else {
//                    res.setAverageRatio(totalGoldTon.divide(totalWeight, 2, RoundingMode.HALF_UP).toString());
//                }
//                res.setTotalGoldTon(totalGoldTon.toString());
//            }
//            return res;
//        }).toList();
        return null;
    }


    /**
     * 后管-车队送货统计
     *
     * @param dto
     * @return
     */
    @Override
    public List<MotorcadeStatisticsVO> motorcadeStatistics(DateDTO dto) {
        return deliveryQualityService.motorcadeStatistics(dto);
    }


    /**
     * 后管-车队加油
     *
     * @param dto
     * @return
     */
    @Override
    public List<MotorcadeRefuelStatisticsVO> motorRefuelStatist(DateDTO dto) {
        return refuelRecordService.motorcadeRefuelStatist(dto);
    }

    /**
     * App-近期活动
     *
     * @return
     */
    @Override
    public RecentVO recent() {
//        // 查询最近一条发车记录
//        Delivery depart = deliveryService.getOne(new LambdaQueryWrapper<>(Delivery.class)
////                .eq(Delivery::getStatus, DeliverStatusEnum.DEPART.getCode())
//                .orderByDesc(Delivery::getDepartureTime)
//                .last("limit 1"));
//        List<RecentInfoVO> recentInfos = Lists.newArrayList();
//        if (ObjUtil.isNotNull(depart)) {
//            Company company = companyService.getById(depart.getCompanyId());
//            User user = userService.getById(depart.getCreateBy());
//            // 组装数据返回
//            StringBuilder sb = new StringBuilder();
//            sb.append(depart.getCarNumber()).append("发往").append(company.getCompanyName());
//            recentInfos.add(RecentInfoVO.builder()
//                    .id(depart.getId())
//                    .subject(RecrntTypeEnum.DEPART.getDesc())
//                    .type(RecrntTypeEnum.DEPART.getCode())
//                    .content(sb.toString())
//                    .createUser(user.getNickName())
//                    .createTime(depart.getDepartureTime())
//                    .build());
//        }
//        // 查询最近一条过磅记录
//        Delivery weigh = deliveryService.getOne(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(Delivery::getStatus, DeliverStatusEnum.WEIGH.getCode())
//                .orderByDesc(Delivery::getWeighingTime)
//                .last("limit 1")
//        );
//        if (ObjUtil.isNotNull(weigh)) {
//            Company company = companyService.getById(weigh.getCompanyId());
//            User user = userService.getById(weigh.getUpdateBy());
//            // 组装数据返回
//            StringBuilder sb = new StringBuilder();
//            sb.append(weigh.getCarNumber()).append("在").append(company.getCompanyName()).append("完成过磅");
//            recentInfos.add(RecentInfoVO.builder()
//                    .id(weigh.getId())
//                    .subject(RecrntTypeEnum.WEIGH.getDesc())
//                    .type(RecrntTypeEnum.WEIGH.getCode())
//                    .content(sb.toString())
//                    .createUser(user.getNickName())
//                    .createTime(weigh.getWeighingTime())
//                    .build());
//        }
//        // 查询最近一条加油记录
//        RefuelRecord refuel = refuelRecordService.getOne(new LambdaQueryWrapper<>(RefuelRecord.class)
//                .orderByDesc(RefuelRecord::getCreateTime)
//                .last("limit 1"));
//        if (ObjUtil.isNotNull(refuel)) {
//            User user = userService.getById(refuel.getUpdateBy());
//            // 组装数据返回
//            StringBuilder sb = new StringBuilder();
//            sb.append(refuel.getCarNumber()).append("加油").append(refuel.getLitre()).append("L");
//            recentInfos.add(RecentInfoVO.builder()
//                    .id(refuel.getId())
//                    .subject(RecrntTypeEnum.REFUEL.getDesc())
//                    .type(RecrntTypeEnum.REFUEL.getCode())
//                    .content(sb.toString())
//                    .createUser(user.getNickName())
//                    .createTime(refuel.getCreateTime())
//                    .build());
//        }
//        // 查询最近一条仓库入库记录
//        // todo 组装数据返回
//        StringBuilder sb = new StringBuilder();
//        sb.append("A仓库").append("入库").append("100").append("kg");
//        recentInfos.add(RecentInfoVO.builder()
//                .id("1")
//                .subject(RecrntTypeEnum.WAREHOUSE.getDesc())
//                .type(RecrntTypeEnum.WAREHOUSE.getCode())
//                .content(sb.toString())
//                .createUser("张三")
//                .createTime(new Date())
//                .build());
//
//
//        return RecentVO.builder()
//                .recentInfoList(recentInfos)
//                .build();
        return null;
    }


    public static void main(String[] args) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("A仓库").append("入库").append("100").append("kg");
        String remark = "[系统管理员:摊取]";
        System.out.println(remark.substring(1, remark.length() - 1));
    }
}
