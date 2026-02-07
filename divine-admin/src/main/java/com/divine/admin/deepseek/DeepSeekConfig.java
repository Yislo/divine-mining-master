package com.divine.admin.deepseek;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * DeepSeek API配置类
 * 包含所有API调用的配置参数
 */
@Data
@Component
public class DeepSeekConfig {
    /**
     * DeepSeek API密钥
     * 从DeepSeek平台获取，注意保密
     */
    @Value("${deepseek.api-key}")
    private String apiKey = "sk-7f775430e5cb490b87dca96e00811565";

    /**
     * API基础URL
     */
    private String apiBaseUrl = "https://api.deepseek.com";

    /**
     * 聊天完成接口路径
     */
    private String chatCompletionsPath = "/v1/chat/completions";

    /**
     * 默认使用的模型名称
     */
    private String defaultModel = "deepseek-chat";

    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 10000;

    /**
     * 读取超时时间（毫秒）
     */
    private int socketTimeout = 30000;

    /**
     * 连接请求超时时间（毫秒）
     */
    private int connectionRequestTimeout = 5000;

    /**
     * 最大重试次数
     */
    private int maxRetries = 3;

    /**
     * 重试延迟时间（毫秒）
     */
    private long retryDelay = 1000;

    /**
     * 连接池最大连接数
     */
    private int maxConnections = 100;

    /**
     * 每个路由最大连接数
     */
    private int maxConnectionsPerRoute = 20;

    /**
     * 是否启用连接池
     */
    private boolean enableConnectionPool = true;

    /**
     * 代理服务器地址
     */
    private String proxyHost = null;

    /**
     * 代理服务器端口
     */
    private Integer proxyPort = null;

    /**
     * 是否启用详细日志
     */
    private boolean enableVerboseLogging = false;

    /**
     * 获取完整的聊天API URL
     */
    public String getChatCompletionsUrl() {
        return apiBaseUrl + chatCompletionsPath;
    }
}
