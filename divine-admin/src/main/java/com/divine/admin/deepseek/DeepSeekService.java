package com.divine.admin.deepseek;


import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.List;

/**
 * DeepSeek API服务接口
 * 提供聊天、文件上传和分析功能
 */
public interface DeepSeekService {

    // ========== 聊天功能 ==========

    /**
     * 同步聊天
     * @param request 聊天请求
     * @return 聊天响应
     */
    ChatResponse chat(ChatRequest request);

    /**
     * 异步聊天
     * @param request 聊天请求
     * @return CompletableFuture包装的聊天响应
     */
    CompletableFuture<ChatResponse> chatAsync(ChatRequest request);

    /**
     * 简化聊天（单条消息）
     * @param message 用户消息
     * @return 助手回复
     */
    String simpleChat(String message);

    /**
     * 带系统提示的聊天
     * @param systemPrompt 系统提示
     * @param userMessage 用户消息
     * @return 助手回复
     */
    String chatWithSystemPrompt(String systemPrompt, String userMessage);

    // ========== 文件功能 ==========

    /**
     * 上传文件
     * @param request 文件上传请求
     * @return 文件ID
     */
    String uploadFile(UploadFileRequest request);

    /**
     * 从本地文件上传
     * @param file 文件对象
     * @param purpose 文件用途
     * @return 文件ID
     */
    String uploadFile(File file, String purpose);

    /**
     * 分析文件内容
     * @param request 文件分析请求
     * @return 分析结果
     */
    String analyzeFiles(FileAnalysisRequest request);

    /**
     * 上传并分析文件
     * @param file 文件对象
     * @param instruction 分析指令
     * @return 分析结果
     */
    String uploadAndAnalyzeFile(File file, String instruction);

    /**
     * 批量上传文件
     * @param files 文件列表
     * @param purpose 文件用途
     * @return 文件ID列表
     */
    List<String> batchUploadFiles(List<File> files, String purpose);

    // ========== 工具方法 ==========

    /**
     * 获取服务配置
     * @return 配置对象
     */
    DeepSeekConfig getConfig();

    /**
     * 测试API连接
     * @return 是否连接成功
     */
    boolean testConnection();

    /**
     * 获取API使用统计
     * @return 统计信息字符串
     */
    String getUsageStatistics();

    /**
     * 关闭服务，释放资源
     */
    void shutdown();
}
