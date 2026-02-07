package com.divine.weighing.controller;


import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.vo.OptionVO;
import com.divine.weighing.service.OptionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 下拉框Controller
 * @Author: yisl
 * @Date: 2025-10-29 18:56:54
 */
@Tag(name = "下拉框相关")
@RestController
@RequestMapping("/options")
public class OptionsController {

    @Autowired
    private OptionsService optionsService;


    @GetMapping
    @Operation(summary = "下拉框")
    public Result<List<OptionVO>> getDropdown(@RequestParam
                                              @Parameter(
                                                      name = "type",
                                                      description = "下拉框类型，可选值：deliverStatus,roleType,owner,car,motorcade,company",
                                                      examples = {
                                                              @ExampleObject(name = "送货状态", value = "deliverStatus"),
                                                              @ExampleObject(name = "加油状态", value = "refuelStatus"),
                                                              @ExampleObject(name = "角色类型", value = "roleType"),
                                                              @ExampleObject(name = "矿主", value = "owner"),
                                                              @ExampleObject(name = "车辆", value = "car"),
                                                              @ExampleObject(name = "车队", value = "motorcade"),
                                                              @ExampleObject(name = "公司", value = "company"),
                                                              @ExampleObject(name = "用户", value = "user")
                                                      }
                                              ) String type) {
        return Result.success(optionsService.getOptionsByType(type));
    }

}
