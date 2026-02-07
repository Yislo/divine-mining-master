package com.divine.weighing.controller;

import com.alibaba.fastjson2.JSON;
import com.divine.common.core.domain.Result;
import com.divine.weighing.domain.dto.export.DeliveryExportDTO;
import com.divine.weighing.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 送货记录Controller
 * @Author: yisl
 * @Date: 2025-11-21 11:05:10
 */
@Tag(name = "数据导出相关")
@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportService exportDelivery;



    @PostMapping("/delivery")
    @Operation(summary = "导出送货记录")
    public void exportDelivery(@RequestBody @Validated DeliveryExportDTO dto,
                               HttpServletResponse response) {
        try {
            exportDelivery.exportDelivery(dto, response);
        } catch (Exception e) {
            try {
                // ⭐ 非常关键：重置 response
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(
                        JSON.toJSONString(
                                Result.fail(
                                        "导出失败：" + e.getMessage()
                                )
                        )
                );
            } catch (Exception ignored) {}
        }
    }

}
