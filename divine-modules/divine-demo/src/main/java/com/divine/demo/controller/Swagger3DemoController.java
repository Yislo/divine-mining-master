package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name="swagger3 用法示例")
@RestController
@RequestMapping("/swagger/demo")
public class Swagger3DemoController {

    @Operation(summary = "上传请求(必须使用 @RequestPart 注解标注为文件)")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        return Result.success("操作成功", file.getOriginalFilename());
    }

}
