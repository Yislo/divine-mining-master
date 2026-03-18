package com.divine.system.controller.system;

import com.divine.common.core.domain.Result;
import com.divine.common.core.enums.OptionTypeEnum;
import com.divine.system.domain.vo.SysConfigVo;
import com.divine.common.core.domain.vo.OptionVO;
import com.divine.system.service.CommonService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共模块
 *
 * @Author: Yisl
 * @Description:
 * @Date: 2026/2/10 9:10
 */
@RequiredArgsConstructor
@RestController
public class CommonController {

    private final CommonService commonService;


    /**
     * 获取下拉框
     * @param type 下拉框类型
     * @return
     */
    @GetMapping("/getOption")
    public Result<List<OptionVO>> getOption(@RequestParam
                                            @Parameter(
                                                description = "下拉框类型",
                                                required = true,
                                                schema = @Schema(implementation = OptionTypeEnum.class)
                                            ) OptionTypeEnum type) {
        return Result.success(commonService.getOption(type.getCode()));
    }

    /**
     * 获取配置参数值
     * @param keyName
     * @return
     */
    @GetMapping("/getConfigParam")
    public Result<SysConfigVo> getConfigParam(@RequestParam String keyName) {
        return Result.success(commonService.getConfigParam(keyName));
    }


}
