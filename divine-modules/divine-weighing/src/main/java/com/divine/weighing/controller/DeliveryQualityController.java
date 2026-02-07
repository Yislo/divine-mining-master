package com.divine.weighing.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.dto.deliveryquality.QualityUpdateDTO;
import com.divine.weighing.domain.dto.deliveryquality.QualityPageDTO;
import com.divine.weighing.domain.vo.deliveryquality.QualityPageVO;
import com.divine.weighing.service.DeliveryQualityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 送货质量Controller
 * @Author: yisl
 * @Date: 2025-12-13 19:54:28
 */
@Tag(name = "质量管理")
@RestController
@RequestMapping("/deliveryQuality")
public class DeliveryQualityController {

    @Autowired
    private DeliveryQualityService deliveryQualityService;


    @PostMapping("/queryQualityPage")
    @Operation(summary = "分页查询质量管理")
    public Result<IPage<QualityPageVO>> queryDeliveryQualityPage(@RequestBody @Validated QualityPageDTO dto) {
        return Result.success(deliveryQualityService.queryDeliveryQualityPage(dto));
    }


    @PutMapping("/updateQualityById")
    @Operation(summary = "根据id修改送货质量")
    public Result<Boolean> updateDeliveryQuality(@RequestBody @Validated QualityUpdateDTO qualityUpdateDTO) {
        return Result.success(deliveryQualityService.updateDeliveryQualityById(qualityUpdateDTO));
    }

}
