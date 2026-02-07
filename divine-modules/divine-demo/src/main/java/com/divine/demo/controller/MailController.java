package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.common.mail.utils.MailUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Tag(name = "邮件发送案例")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/mail")
public class MailController {

    @Operation(summary = "发送邮件")
    @GetMapping("/sendSimpleMessage")
    public Result<Void> sendSimpleMessage(@Schema(description = "接收人") String to,
                                          @Schema(description = "标题")String subject,
                                          @Schema(description = "内容")String text) {
        MailUtils.sendText(to, subject, text);
        return Result.success();
    }

    @Operation(summary = "发送邮件（带附件）")
    @GetMapping("/sendMessageWithAttachment")
    public Result<Void> sendMessageWithAttachment(@Schema(description = "接收人")String to,
                                                  @Schema(description = "标题")String subject,
                                                  @Schema(description = "内容")String text,
                                                  @Schema(description = "附件路径")String filePath) {
        MailUtils.sendText(to, subject, text, new File(filePath));
        return Result.success();
    }

}
