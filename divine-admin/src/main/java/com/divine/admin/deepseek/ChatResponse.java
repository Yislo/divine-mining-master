package com.divine.admin.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 聊天响应实体类
 * 包含DeepSeek API返回的完整响应信息
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)  // 忽略null值字段
@JsonIgnoreProperties(ignoreUnknown = true)  // 关键：忽略所有未知字段
public class ChatResponse {

    /**
     * 响应ID
     * 唯一标识本次API调用
     */
    @JsonProperty("id")
    private String id;

    /**
     * 对象类型
     * 固定为"chat.completion"
     */
    @JsonProperty("object")
    private String object;

    /**
     * 响应创建时间戳（秒）
     */
    @JsonProperty("created")
    private Long created;

    /**
     * 使用的模型名称
     */
    @JsonProperty("model")
    private String model;

    /**
     * 回复选择列表
     * 包含一个或多个候选回复
     */
    @JsonProperty("choices")
    private List<Choice> choices;

    /**
     * Token使用统计
     */
    @JsonProperty("usage")
    private Usage usage;

    /**
     * 系统指纹（用于调试）
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 候选回复选择项
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {

        /**
         * 选择项的索引
         */
        @JsonProperty("index")
        private Integer index;

        /**
         * 回复消息
         */
        @JsonProperty("message")
        private Message message;

        /**
         * 生成结束原因
         * 可能值: "stop", "length", "content_filter"
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 流式响应时的增量内容（仅流式模式）
         */
        @JsonProperty("delta")
        private Message delta;

        /**
         * 日志概率（可选字段） - 根据你的错误日志添加
         */
        @JsonProperty("logprobs")
        private Object logprobs;  // 使用Object接收未知类型
    }

    /**
     * Token使用统计
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {

        /**
         * 提示词消耗的token数量
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        /**
         * 回复消耗的token数量
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        /**
         * 总token消耗量
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;

        /**
         * 提示token详情（可选） - 根据你的错误日志添加
         */
        @JsonProperty("prompt_tokens_details")
        private Map<String, Object> promptTokensDetails;  // 使用Map接收未知结构

        /**
         * 提示缓存命中token数（可选）
         */
        @JsonProperty("prompt_cache_hit_tokens")
        private Integer promptCacheHitTokens;

        /**
         * 提示缓存未命中token数（可选）
         */
        @JsonProperty("prompt_cache_miss_tokens")
        private Integer promptCacheMissTokens;
    }

    /**
     * 获取第一个回复内容
     * @return 回复文本内容，如果没有回复则返回null
     */
    public String getFirstContent() {
        if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null) {
            return choices.get(0).getMessage().getContent();
        }
        return null;
    }

    /**
     * 获取总token消耗
     */
    public int getTotalTokens() {
        return usage != null && usage.getTotalTokens() != null ? usage.getTotalTokens() : 0;
    }

    /**
     * 是否成功获取回复
     */
    public boolean hasContent() {
        return getFirstContent() != null;
    }
}
