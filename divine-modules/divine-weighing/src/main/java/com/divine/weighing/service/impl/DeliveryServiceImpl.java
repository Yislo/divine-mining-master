package com.divine.weighing.service.impl;

import com.baomidou.lock.LockTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.service.UserService;
import com.divine.common.redis.utils.RedisUtils;
import com.divine.weighing.domain.dto.delivery.*;
import com.divine.weighing.domain.entity.Delivery;
import com.divine.weighing.domain.vo.delivery.*;
import com.divine.weighing.domain.vo.home.PieChartVO;
import com.divine.weighing.export.DeliveryExportField;
import com.divine.weighing.service.CarService;
import com.divine.weighing.service.DeliveryQualityService;
import com.divine.weighing.service.DeliveryService;
import com.divine.weighing.domain.dto.home.DateDTO;
import com.divine.weighing.domain.vo.home.CartogramInfoVO;
import com.divine.weighing.mapper.DeliveryMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * @Description: 送货记录业务层
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Service
public class DeliveryServiceImpl extends ServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;




    /**
     * 日期格式化器（固定8位：yyyyMMdd）
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 发车
     *
     * @param departureSaveDTO 发车记录
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean departure(DepartureSaveDTO departureSaveDTO) {
//        Owner owner = ownerService.getById(departureSaveDTO.getOwnerId());
//        if (ObjUtil.isNull(owner)) {
//            throw new BusinessException("矿主不存在");
//        }
//        List<String> carNumberList = departureSaveDTO.getCarList().stream().map(DepartureDTO::getCarNumber).toList();
//        Company company = companyService.getById(departureSaveDTO.getCompanyId());
//        if (ObjUtil.isNull(company)) {
//            throw new BusinessException("工厂信息不存在");
//        }
//        // 查询车辆是否已经发车
//        List<Delivery> depChecks = deliveryMapper.selectList(new LambdaQueryWrapper<Delivery>()
//                .in(Delivery::getCarNumber, carNumberList)
//                .eq(Delivery::getStatus, DeliverStatusEnum.DEPART.getCode())
//        );
//        if (CollectionUtil.isNotEmpty(depChecks)) {
//            List<String> car = depChecks.stream().map(Delivery::getCarNumber).toList();
//            throw new BusinessException("以下车辆信息有误：" + car);
//        }
//        // 查询车辆是否存在
//        List<Car> cars = carService.list(new LambdaQueryWrapper<>(Car.class).in(Car::getCarNumber, carNumberList));
//        if (CollectionUtil.isEmpty(cars)) {
//            throw new BusinessException("车辆信息有误");
//        }
//        Map<String, String> motorcadeMap = cars.stream().collect(Collectors.toMap(Car::getCarNumber, Car::getMotorcadeId));
//        // 组装数据返回
//        List<Delivery> deliveryList = departureSaveDTO.getCarList().stream().map(car -> {
//            Delivery delivery = Delivery.builder()
//                    .carNumber(car.getCarNumber())
//                    .ownerId(departureSaveDTO.getOwnerId())
//                    .companyId(departureSaveDTO.getCompanyId())
//                    .departureTime(departureSaveDTO.getDepartureTime())
//                    .build();
//            String remark = car.getRemark();
//            if (StringUtils.isNotBlank(remark)) {
//                UserInfoVO userInfo = userService.getUserInfo();
//                delivery.setRemark(Collections.singletonList(userInfo.getNickName() + ":" + remark).toString());
//            }
//            delivery.setMotorcadeId(motorcadeMap.get(car.getCarNumber()));
//            return delivery;
//        }).toList();
//        deliveryMapper.insert(deliveryList);
//        // 组装质量表数据
//        List<DeliveryQuality> list = deliveryList.stream().map(d -> DeliveryQuality.builder()
//                .deliveryId(d.getId())
//                .ownerId(departureSaveDTO.getOwnerId())
//                .companyId(departureSaveDTO.getCompanyId())
//                .build()).toList();
//        deliveryQualityService.saveBatch(list);
        return true;
    }


    /**
     * 过磅
     *
     * @param dto 发车记录
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean weighing(WeighingDTO dto) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        // 查询送货记录
//        Delivery delivery = deliveryMapper.selectById(dto.getId());
//        if (ObjUtil.isNull(delivery)) {
//            throw new BusinessException("数据有误");
//        }
////        // 如果是新批次 则生成新的批次编号
////        String batchCode;
////        if (Objects.equals(dto.getIsNewBatch(), YesOrNoEnum.YES.getCode())) {
////            String companyId = deliveryList.get(0).getCompanyId();
////            batchCode = getNextBatchCode(companyId);
////            Company company = companyService.getById(companyId);
////            String prefix = Constant.XM + company.getCompanyCode();
////            redisTemplateUtils.set(RedisKeyConstants.BATCH_CODE + prefix, batchCode, getSecondsUntilMidnight());
////        } else {
////            // 否则获取历史的
////            batchCode = getBatchCode(deliveryList.get(0).getCompanyId());
////        }
////        deliveryList.forEach(d -> {
//        // 历史备注
//        String oldRemark = delivery.getRemark();
//        // 新备注
//        String newRemark = dto.getRemark();
//        if (StringUtils.isNotBlank(newRemark) && StringUtils.isNotBlank(oldRemark)) {
//            List<String> remarks = stringToList(oldRemark);
//            remarks.add(userInfo.getNickName() + ":" + newRemark);
//            delivery.setRemark(remarks.toString());
//        } else if (StringUtils.isBlank(oldRemark) && StringUtils.isNotBlank(newRemark)) {
//            delivery.setRemark(Collections.singletonList(userInfo.getNickName() + ":" + dto.getRemark()).toString());
//        }
//        // 获取公司编号
//        Company company = companyService.getById(delivery.getCompanyId());
//        delivery.setWeightNo(getCode(company));
//        delivery.setWeighingTime(new Date());
//        delivery.setStatus(DeliverStatusEnum.WEIGH.getCode());
//        fileService.batchSaveFile(FileBatchSaveDTO.builder()
//                .businessId(delivery.getId())
//                .businessType(BusinessTypeEnum.WEIGH.getValue())
//                .fileList(dto.getFileList())
//                .build());
//        // 更新质量表数据
//        deliveryQualityService.update(DeliveryQuality.builder()
//                        .deliveryId(delivery.getId())
//                        .totalWeight(dto.getTotalWeight())
//                        .build(),
//                new LambdaQueryWrapper<>(DeliveryQuality.class)
//                        .eq(DeliveryQuality::getDeliveryId, delivery.getId()));
//        deliveryMapper.updateById(delivery);
        return true;
    }


    /**
     * 将 ArrayList.toString() 生成的字符串转回 List<String>
     *
     * @param str 格式如 [a, b, c] 或 []
     * @return 可修改的 ArrayList<String>
     */
    public static List<String> stringToList(String str) {
        List<String> list = new ArrayList<>();

        // 处理空集合（str 为 "[]" 时直接返回空列表）
        if (str == null || "[]".equals(str.trim())) {
            return list;
        }

        // 步骤1：去除首尾的 [ 和 ]
        String content = str.trim().substring(1, str.trim().length() - 1);

        // 步骤2：按 ", " 分割元素（处理多个元素的情况）
        if (!content.isEmpty()) { // 避免空字符串分割（比如原集合只有1个元素时，content 不为空）
            String[] elements = content.split(", ");
            // 步骤3：转为可修改的 ArrayList（Arrays.asList() 返回固定大小集合，需包装）
            list.addAll(Arrays.asList(elements));
        }

        return list;
    }


    /**
     * 回磅
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean returnWeighing(WeighingWeightDTO dto) {
//        String id = dto.getId();
//        DeliveryQuality dq = deliveryQualityService.getOne(new LambdaQueryWrapper<>(DeliveryQuality.class)
//                .eq(DeliveryQuality::getDeliveryId, dto.getId()));
//        dq.setTotalWeight(dto.getTotalWeight());
//        dq.setNetWeight(dto.getNetWeight());
//        dq.setEmptyWeight(dto.getEmptyWeight());
//        // 修改质量表
//        deliveryQualityService.updateById(dq);
//        // 修改送货状态
//        Delivery delivery = deliveryMapper.selectById(id);
//        delivery.setStatus(DeliverStatusEnum.COMPLETE.getCode());
//        // 存储文件
//        fileService.batchSaveFile(FileBatchSaveDTO.builder()
//                .businessId(id)
//                .businessType(BusinessTypeEnum.RETURN_WEIGH.getValue())
//                .fileList(dto.getFileList())
//                .build());
//        deliveryMapper.updateById(delivery);
        return true;
    }

    /**
     * 保存取样文件
     *
     * @param dto
     * @return
     */
    @Override
    public boolean saveSampleFile(SaveSampleFileDTO dto) {
//        // 存储文件
//        fileService.batchSaveFile(FileBatchSaveDTO.builder()
//                .businessId(dto.getId())
//                .businessType(BusinessTypeEnum.WEIGH.getValue())
//                .fileList(dto.getFileList())
//                .build());
        return true;
    }



    /**
     * 获取距离当天24:00的剩余秒数
     */
    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.plusDays(1).with(LocalTime.MIN); // 次日00:00（等价于当天24:00）
        return ChronoUnit.SECONDS.between(now, midnight);
    }

    /**
     * 获取当前批次号
     *
     * @return
     */
    @Override
    public String getBatchCode(String companyId) {
//        Company company = companyService.getById(companyId);
//        String prefix = Constant.XM + company.getCompanyCode();
//        String lastCode = (String) redisUtils.get(RedisKeyConstants.BATCH_CODE + prefix);
//        if (StringUtils.isBlank(lastCode)) {
//            return getCode(company);
//        }
//        return lastCode;
        return null;
    }


    /**
     * 获取下一个批次号
     *
     * @return
     */
    @Override
    public String getNextBatchCode(String companyId) {
//        Company company = companyService.getById(companyId);
//        String prefix = Constant.XM + company.getCompanyCode();
//        String lastCode = (String) redisUtils.get(RedisKeyConstants.BATCH_CODE + prefix);
//        if (StringUtils.isBlank(lastCode)) {
//            return getCode(company);
//        }
//        // 判断最后3位数如果是001直接返回
//        if ("001".equals(lastCode.substring(lastCode.length() - 3))) {
//            return lastCode;
//        }
//        return prefix + lastCode.substring(prefix.length(), lastCode.length() - 3)
//                + String.format("%03d", Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1);
        return null;
    }

    /**
     * 分页查询发货记录(按车辆)
     *
     * @param departureDTO 查询条件
     * @return 查询结果
     */
    @Override
    public IPage<DeliveryCarPageVO> deliveryCarPage(DeparturePageDTO departureDTO) {
//        // 获取当前登录用户的用户信息
//        UserInfoVO userInfo = userService.getUserInfo();
//        String companyId = userInfo.getCompanyId();
//        // 判断当前用户是否管理员
//        boolean adminOrLeader = RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode());
//        // 构建 MP 分页对象（传入当前页、每页条数）
//        IPage<DeliveryCarPageVO> resultPage = new Page<>(departureDTO.getPageNum(), departureDTO.getPageSize());
//        // 调用 Mapper 分页查询（MP 自动拦截，拼接分页 SQL）
//        if (!adminOrLeader) {
//            if (StringUtils.isBlank(companyId)) {
//                return resultPage;
//            }
//            departureDTO.setCompanyId(companyId);
//        }
//        resultPage = baseMapper.deliveryCarPage(resultPage, departureDTO);
//        resultPage.getRecords().forEach(
//                r -> {
//                    String weightNo = r.getWeightNo();
//                    if (weightNo.isBlank()){
//                        r.setWeightNo(DEFAULT_EMPTY_DATA);
//                    }
//                    String remark = r.getRemark();
//                    if (StringUtils.isNotBlank(remark)) {
//                        r.setRemarkList(stringToList(remark));
//                    }
//                });
        return null;

    }

    /**
     * 获取送货数量
     *
     * @return
     */
    @Override
    public DeliveryNumVO getDeliveryNum() {
//        // todo 优化存缓存
//        UserInfoVO userInfo = userService.getUserInfo();
//        boolean adminOrLeader = RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode());
//        //全部
//        Long allNum = deliveryMapper.selectCount(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(!adminOrLeader, Delivery::getCompanyId, userInfo.getCompanyId()));
//        // 已发车
//        Long departureNum = deliveryMapper.selectCount(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(Delivery::getStatus, DeliverStatusEnum.DEPART.getCode())
//                .eq(!adminOrLeader, Delivery::getCompanyId, userInfo.getCompanyId()));
//        // 已过磅
//        Long weightNum = deliveryMapper.selectCount(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(Delivery::getStatus, DeliverStatusEnum.WEIGH.getCode())
//                .eq(!adminOrLeader, Delivery::getCompanyId, userInfo.getCompanyId()));
//        // 已完成
//        Long completeNum = deliveryMapper.selectCount(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(Delivery::getStatus, DeliverStatusEnum.COMPLETE.getCode())
//                .eq(!adminOrLeader, Delivery::getCompanyId, userInfo.getCompanyId()));
//        return DeliveryNumVO.builder()
//                .allNum(allNum)
//                .departureNum(departureNum)
//                .weightNum(weightNum)
//                .completeNum(completeNum)
//                .build();
        return null;
    }

    /**
     * 分页查询发货记录(按批次)
     *
     * @param departureDTO 查询条件
     * @return 查询结果
     */
    @Override
    public IPage<DeliveryBatchPageVO> deliveryBatchPage(DeparturePageDTO departureDTO) {
//        // 构建 MP 分页对象（传入当前页、每页条数）
//        IPage<DeliveryBatchPageVO> resultPage = new Page<>(departureDTO.getPageNum(), departureDTO.getPageSize());
//        // 调用 Mapper 分页查询（MP 自动拦截，拼接分页 SQL）
//        resultPage = baseMapper.deliverBatchPage(resultPage, departureDTO);
//        // 当前状态
//        List<DeliveryBatchPageVO> records = resultPage.getRecords();
//        records.forEach(r -> {
//            Long l = deliveryMapper.selectCount(new LambdaQueryWrapper<>(Delivery.class)
//                    .ne(Delivery::getStatus, DeliverStatusEnum.COMPLETE.getCode())
//                    .eq(Delivery::getBatchCode, r.getBatchCode())
//            );
//            if (l > 0) {
//                r.setStatus(0);
//                r.setStatusText(" ");
//            } else {
//                r.setStatus(1);
//                r.setStatusText("已完成");
//            }
//        });
//        return resultPage;
        return null;
    }


    /**
     * 修改送货记录
     *
     * @param deliveryUpdateDTO 送货记录
     * @return 修改结果
     */
    @Override
    public boolean updateDeliveryById(DeliveryUpdateDTO deliveryUpdateDTO) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        boolean adminOrLeader = RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode());
//        if (!adminOrLeader) {
//            throw new BusinessException("暂无修改权限");
//        }
//        String lockKey = deliveryUpdateDTO.getId();
//        String requestId = UUID.randomUUID().toString();
//        boolean locked = lockTemplate.lock(lockKey, requestId, 10, 3);
//        if (!locked) {
//            throw new BusinessException("系统繁忙，请稍后再试");
//        }
//        try {
//            Delivery delivery = BeanUtil.copyProperties(deliveryUpdateDTO, Delivery.class);
//            deliveryMapper.updateById(delivery);
//        } finally {
//            lockTemplate.unlock(lockKey, requestId);
//        }

        return true;
    }

    /**
     * 统计所有工厂发车记录
     *
     * @param dateDTO 时间
     * @return
     */
    @Override
    public List<CartogramInfoVO> cartogram(DateDTO dateDTO) {
        return deliveryMapper.cartogram(dateDTO);
    }

    /**
     * 统计单个工厂发车记录
     *
     * @param dateDTO 时间
     * @return 发车记录
     */
    @Override
    public PieChartVO pieChart(DateDTO dateDTO, String companyId) {
        return deliveryMapper.pieChart(dateDTO, companyId);
    }

    /**
     * 获取送货记录详情
     *
     * @param id
     * @return
     */
    @Override
    public DeliveryCarInfoVO getDeliveryInfo(String id) {
//        UserInfoVO userInfo = userService.getUserInfo();
//        Delivery delivery = deliveryMapper.selectById(id);
//        DeliveryQuality dq = deliveryQualityService.getOne(new LambdaQueryWrapper<>(DeliveryQuality.class)
//                .eq(DeliveryQuality::getDeliveryId, id));
//        // 查询相关文件
//        List<FileVO> file = fileService.getFile(FileQueryDTO.builder().businessId(id).build());
//        String companyId = delivery.getCompanyId();
//        // 获取工厂信息
//        Company company = companyService.getById(companyId);
//        // 获取矿主信息
//        Owner owner = ownerService.getById(delivery.getOwnerId());
//        // 获取车队信息
//        Motorcade motorcade = motorcadeService.getById(delivery.getMotorcadeId());
//        // 获取用户信息
//        Integer totalWeight = dq.getTotalWeight();
//        Integer emptyWeight = dq.getEmptyWeight();
//        Integer netWeight = dq.getNetWeight();
//        BigDecimal moisture = dq.getMoisture();
//        BigDecimal cuoRatio = dq.getCuoRatio();
//        BigDecimal acidDemand = dq.getAcidDemand();
//        User user = userService.getById(delivery.getCreateBy());
//        DeliveryCarInfoVO deliveryCarInfoVO = BeanUtil.copyProperties(delivery, DeliveryCarInfoVO.class);
//        deliveryCarInfoVO.setFileList(file);
//        deliveryCarInfoVO.setCompanyName(company.getCompanyName());
//        deliveryCarInfoVO.setOwnerName(owner.getName());
//        deliveryCarInfoVO.setMotorcadeName(motorcade.getMotorcadeName());
//        deliveryCarInfoVO.setCreateByName(user.getNickName());
//        deliveryCarInfoVO.setStatusText(DeliverStatusEnum.getDescByCode(delivery.getStatus()));
//        deliveryCarInfoVO.setTotalWeight(totalWeight == null ? DEFAULT_EMPTY_DATA : totalWeight.toString());
//        deliveryCarInfoVO.setNetWeight(netWeight == null ? DEFAULT_EMPTY_DATA : netWeight.toString());
//        deliveryCarInfoVO.setEmptyWeight(emptyWeight == null ? DEFAULT_EMPTY_DATA : emptyWeight.toString());
//        deliveryCarInfoVO.setMoisture(moisture == null ? DEFAULT_EMPTY_DATA : moisture + "%");
//        deliveryCarInfoVO.setCuoRatio(cuoRatio == null ? DEFAULT_EMPTY_DATA : cuoRatio + "%");
//        deliveryCarInfoVO.setAcidDemand(acidDemand == null ? DEFAULT_EMPTY_DATA : acidDemand.toString());
//        String remark = delivery.getRemark();
//        if (StringUtils.isNotBlank(remark)) {
//            deliveryCarInfoVO.setRemarkList(stringToList(remark));
//        }
//        // 计算金吨 金吨=净重*（100-水）/100*氧化铜/100
//        if (ObjUtil.isNotNull(netWeight)
//                && ObjUtil.isNotNull(moisture)
//                && ObjUtil.isNotNull(cuoRatio)
//                && RoleTypeEnum.isAdminOrLeader(userInfo.getRoleCode())) {
//            BigDecimal goldTon = new BigDecimal(netWeight)
//                    .multiply(BigDecimal.valueOf(100).subtract(moisture))
//                    .multiply(cuoRatio)
//                    .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
//            deliveryCarInfoVO.setGoldTon(goldTon.toString());
//        } else {
//            deliveryCarInfoVO.setGoldTon(DEFAULT_EMPTY_DATA);
//        }
//        DeliveryCarInfoVO.fillDefaultValues(deliveryCarInfoVO);
//        return deliveryCarInfoVO;
        return null;
    }

    /**
     * 获取批次信息
     *
     * @param batchCode 批次号
     * @return 批次信息
     */
    @Override
    public DeliveryBatchInfoVO getBatchInfo(String batchCode) {
//        List<Delivery> deliveries = deliveryMapper.selectList(new LambdaQueryWrapper<>(Delivery.class)
//                .eq(Delivery::getBatchCode, batchCode));
//        // 获取工厂信息
//        Company company = companyService.getById(deliveries.get(0).getCompanyId());
//        DeliveryBatchInfoVO res = new DeliveryBatchInfoVO();
//        res.setBatchCode(batchCode);
//        res.setCompanyName(company.getCompanyName());
//        res.setCompanyId(company.getId());
//        List<DeliveryInfoVO> list = deliveries.stream().map(d -> {
//            // 查询相关文件
//            List<FileVO> file = fileService.getFile(FileQueryDTO.builder().businessId(d.getId()).build());
//            String companyId = d.getCompanyId();
//            // 获取矿主信息
//            Owner owner = ownerService.getById(d.getOwnerId());
//            // 获取车队信息
//            Motorcade motorcade = motorcadeService.getById(d.getMotorcadeId());
//            // 获取用户信息
//            User user = userService.getById(d.getCreateBy());
//            DeliveryInfoVO deliveryInfo = BeanUtil.copyProperties(d, DeliveryInfoVO.class);
//            deliveryInfo.setFileList(file);
//            deliveryInfo.setOwnerName(owner.getName());
//            deliveryInfo.setMotorcadeName(motorcade.getMotorcadeName());
//            deliveryInfo.setCreateByName(user.getNickName());
//            deliveryInfo.setStatusText(DeliverStatusEnum.getDescByCode(d.getStatus()));
//            String remark = d.getRemark();
//            if (StringUtils.isNotBlank(remark)) {
//                deliveryInfo.setRemarkList(stringToList(remark));
//            }
//            DeliveryInfoVO.fillDefaultValues(deliveryInfo);
//            return deliveryInfo;
//        }).toList();
//        res.setDeliveryList(list);
//        return res;
        return null;
    }

    /**
     * 逻辑删除送货记录
     *
     * @param id 主键id
     * @return 删除结果
     */
    @Override
    public boolean deleteDeliveryById(String id) {
        int deleteNum = deliveryMapper.deleteById(id);
        return deleteNum > 0;
    }

    /**
     * 查询导出数据
     *
     * @param departureDTO 查询条件
     * @return
     */
    @Override
    public IPage<DeliveryExportField> selectExportList(DeparturePageDTO departureDTO) {
        IPage<DeliveryExportField> resultPage = new Page<>(departureDTO.getPageNum(), departureDTO.getPageSize());
        resultPage = baseMapper.selectExportList(resultPage, departureDTO);
        return resultPage;
    }

}
