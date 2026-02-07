package com.divine.admin.deepseek;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 聊天请求实体类
 * 包含调用DeepSeek聊天API的所有参数
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    /**
     * 要使用的模型ID
     * 例如: "deepseek-chat", "deepseek-coder"等
     */
    @JsonProperty("model")
    private String model;

    /**
     * 消息列表
     * 按时间顺序排列的对话消息
     */
    @JsonProperty("messages")
    private List<Message> messages;

    /**
     * 采样温度
     * 范围: 0.0 - 2.0
     * 值越高，回复越随机和创造性
     * 值越低，回复越确定和一致
     */
    @JsonProperty("temperature")
    private Double temperature = 0.7;

    /**
     * Top-p采样参数
     * 范围: 0.0 - 1.0
     * 控制输出的多样性
     */
    @JsonProperty("top_p")
    private Double topP = 1.0;

    /**
     * 生成的最大token数
     * 注意：输入token + 输出token不能超过模型限制
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 是否启用流式输出
     * 如果为true，响应将以流的形式返回
     */
    @JsonProperty("stream")
    private Boolean stream = false;

    /**
     * 停止标记
     * 当生成这些字符串时停止生成
     */
    @JsonProperty("stop")
    private List<String> stop;

    /**
     * 存在惩罚
     * 范围: -2.0 到 2.0
     * 正值惩罚已经出现过的token
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty = 0.0;

    /**
     * 频率惩罚
     * 范围: -2.0 到 2.0
     * 正值惩罚频繁出现的token
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty = 0.0;

    /**
     * 返回的候选回复数量
     * 注意：每个候选回复都会消耗token
     */
    @JsonProperty("n")
    private Integer n = 1;

    /**
     * 构造方法
     */
    public ChatRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    /**
     * 快捷创建用户请求
     */
    public static ChatRequest createUserRequest(String model, String userMessage) {
        Message message = Message.createUserMessage(userMessage);
        return new ChatRequest(model, List.of(message));
    }

    /**
     * 添加系统提示
     */
    public void addSystemPrompt(String systemPrompt) {
        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            Message systemMessage = Message.createSystemMessage(systemPrompt);
            if (messages != null && !messages.isEmpty()) {
                messages.add(0, systemMessage);
            } else {
                messages = List.of(systemMessage);
            }
        }
    }
}
