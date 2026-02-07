package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;

@Tag(name = "短信演示案例")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/sms")
public class SmsController {

    @Operation(summary = "发送短信Aliyun")
    @GetMapping("/sendAliyun")
    public Result<Object> sendAliyun(@Schema(description = "电话号")String phones,
                                     @Schema(description = "模板ID")String templateId) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("code", "1234");
        SmsBlend smsBlend = SmsFactory.createSmsBlend(SupplierType.ALIBABA);
        SmsResponse smsResponse = smsBlend.sendMessage(phones, templateId, map);
        return Result.success(smsResponse);
    }

    @Operation(summary = "发送短信Tencent")
    @GetMapping("/sendTencent")
    public Result<Object> sendTencent(@Schema(description = "电话号") String phones,
                                      @Schema(description = "模板ID") String templateId) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
//        map.put("2", "测试测试");
        map.put("1", "1234");
        SmsBlend smsBlend = SmsFactory.createSmsBlend(SupplierType.TENCENT);
        SmsResponse smsResponse = smsBlend.sendMessage(phones, templateId, map);
        return Result.success(smsResponse);
    }

}
