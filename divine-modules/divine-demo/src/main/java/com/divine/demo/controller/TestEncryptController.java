package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.demo.domain.entity.TestDemoEncrypt;
import com.divine.demo.mapper.TestDemoEncryptMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "测试数据库加解密功能")
@Validated
@RestController
@RequestMapping("/demo/encrypt")
public class TestEncryptController {

    @Autowired
    private TestDemoEncryptMapper mapper;

    @Value("${mybatis-encryptor.enable}")
    private Boolean encryptEnable;

    @Operation(summary = "测试数据库加解密")
    @GetMapping()
    public Result<Map<String, TestDemoEncrypt>> test(@Schema(description = "测试key") String key,
                                                     @Schema(description = "测试value") String value) {
        if (!encryptEnable) {
            throw new RuntimeException("加密功能未开启!");
        }
        Map<String, TestDemoEncrypt> map = new HashMap<>(2);
        TestDemoEncrypt demo = new TestDemoEncrypt();
        demo.setTestKey(key);
        demo.setValue(value);
        mapper.insert(demo);
        map.put("加密", demo);
        TestDemoEncrypt testDemo = mapper.selectById(demo.getId());
        map.put("解密", testDemo);
        return Result.success(map);
    }


}
