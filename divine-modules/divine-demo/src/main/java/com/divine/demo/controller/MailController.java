package com.divine.demo.controller;

import com.divine.common.core.domain.Result;
import com.divine.common.mail.utils.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.UUID;

/**
 * 邮件发送案例
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/demo/mail")
public class MailController {

    public static void main(String[] args) {
        System.out.println(UUID.randomUUID());
    }

    /**
     * 发送邮件
     *
     * @param to      接收人
     * @param subject 标题
     * @param text    内容
     * @return
     */
    @GetMapping("/sendSimpleMessage")
    public Result<Void> sendSimpleMessage(String to,
                                          String subject,
                                          String text) {
        MailUtils.sendText(to, subject, text);
        return Result.success();
    }

    /**
     * 发送邮件（带附件）
     *
     * @param to       接收人
     * @param subject  标题
     * @param text     内容
     * @param filePath 附件路径
     * @return
     */
    @GetMapping("/sendMessageWithAttachment")
    public Result<Void> sendMessageWithAttachment(String to,
                                                  String subject,
                                                  String text,
                                                  String filePath) {
        MailUtils.sendText(to, subject, text, new File(filePath));
        return Result.success();
    }

}
