package com.divine.admin.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 聊天消息实体类
 * 表示对话中的一条消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 消息角色
     * 可选值: "system", "user", "assistant"
     * - system: 系统消息，用于设置助手行为
     * - user: 用户消息
     * - assistant: 助手回复消息
     */
    @JsonProperty("role")
    private String role;

    /**
     * 消息内容
     * 包含实际的文本内容，支持多行文本
     */
    @JsonProperty("content")
    private String content;

    /**
     * 快捷创建系统消息的静态方法
     */
    public static Message createSystemMessage(String content) {
        return new Message("system", content);
    }

    /**
     * 快捷创建用户消息的静态方法
     */
    public static Message createUserMessage(String content) {
        return new Message("user", content);
    }

    /**
     * 快捷创建助手消息的静态方法
     */
    public static Message createAssistantMessage(String content) {
        return new Message("assistant", content);
    }
}
