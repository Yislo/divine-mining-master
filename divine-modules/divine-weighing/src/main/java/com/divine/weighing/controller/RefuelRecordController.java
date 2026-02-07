package com.divine.weighing.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.dto.refuel.RefuelPageDTO;
import com.divine.weighing.domain.dto.refuel.RefuelRecordDTO;
import com.divine.weighing.domain.vo.refuel.RefuelPageVO;
import com.divine.weighing.domain.dto.refuel.RefuelSaveDTO;
import com.divine.weighing.service.RefuelRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 加油记录Controller
 * @Author: yisl
 * @Date: 2025-11-03 09:15:18
 */
@Tag(name = "加油记录相关")
@RestController
@RequestMapping("/refuel")
public class RefuelRecordController {

    @Autowired
    private RefuelRecordService refuelRecordService;

    @PostMapping("/saveRefuelRecord")
    @Operation(summary = "新增加油记录")
    public Result<Boolean> saveRefuelRecord(@RequestBody @Validated RefuelSaveDTO refuelRecordDTO) {
        return Result.success(refuelRecordService.saveRefuelRecord(refuelRecordDTO));
    }

    @PostMapping("/queryRefuelRecordPage")
    @Operation(summary = "分页查询加油记录")
    public Result<IPage<RefuelPageVO>> queryRefuelRecordPage(@RequestBody @Validated RefuelPageDTO refuelRecordDTO) {
        return Result.success(refuelRecordService.queryRefuelRecordPage(refuelRecordDTO));
    }

    @PostMapping("/batchSettlement")
    @Operation(summary = "批量结算")
    public Result<Boolean> batchSettlement(@RequestBody @Validated RefuelRecordDTO refuelRecordDTO) {
        return Result.success(refuelRecordService.batchSettlement(refuelRecordDTO));
    }

    @DeleteMapping("/deleteRefuelRecordById")
    @Operation(summary = "删除加油记录")
    public Result<Boolean> deleteRefuelRecordById(@RequestParam("id") String id) {
        return Result.success(refuelRecordService.deleteRefuelRecordById(id));
    }
}
