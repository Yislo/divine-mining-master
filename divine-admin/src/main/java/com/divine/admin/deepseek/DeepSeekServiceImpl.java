package com.divine.admin.deepseek;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DeepSeek API服务实现类
 * 实现所有聊天和文件操作功能
 */
@Slf4j
@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    private final DeepSeekConfig config;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;
    private final PoolingHttpClientConnectionManager connectionManager;

    // 统计信息
    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger failedRequests = new AtomicInteger(0);
    private final AtomicLong totalTokensUsed = new AtomicLong(0);
    private final AtomicLong totalResponseTime = new AtomicLong(0);

    public DeepSeekServiceImpl(DeepSeekConfig config) {
        this.config = config;
        config.setApiKey("sk-7f775430e5cb490b87dca96e00811565");
        this.objectMapper = new ObjectMapper();

        // 创建连接池管理器
        this.connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());

        // 创建HTTP客户端
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(config.getConnectTimeout())
                .setSocketTimeout(config.getSocketTimeout())
                .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                .build();

        this.httpClient = HttpClients.custom()
                .setConnectionManager(config.isEnableConnectionPool() ? connectionManager : null)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new StandardHttpRequestRetryHandler(config.getMaxRetries(), true))
                .build();

        // 创建线程池
        this.executorService = Executors.newFixedThreadPool(
            Math.max(4, Runtime.getRuntime().availableProcessors() * 2),
            new ThreadFactory() {
                private final AtomicInteger threadCount = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("deepseek-client-" + threadCount.incrementAndGet());
                    thread.setDaemon(true);
                    return thread;
                }
            }
        );

        log.info("DeepSeek服务初始化完成，API基础URL: {}", config.getApiBaseUrl());
        log.info("连接池配置: 最大连接数={}, 每路由最大连接数={}",
                config.getMaxConnections(), config.getMaxConnectionsPerRoute());
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        long startTime = System.currentTimeMillis();
        totalRequests.incrementAndGet();

        try {
            log.debug("发送聊天请求，模型: {}, 消息数: {}",
                    request.getModel(),
                    request.getMessages() != null ? request.getMessages().size() : 0);

            HttpPost httpPost = createChatHttpPost(request);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                ChatResponse chatResponse = handleChatResponse(response);

                // 记录统计信息
                long endTime = System.currentTimeMillis();
                totalResponseTime.addAndGet(endTime - startTime);

                if (chatResponse.getUsage() != null && chatResponse.getUsage().getTotalTokens() != null) {
                    totalTokensUsed.addAndGet(chatResponse.getUsage().getTotalTokens());
                }

                log.debug("聊天请求完成，耗时: {}ms, 使用token: {}",
                        (endTime - startTime),
                        chatResponse.getTotalTokens());

                return chatResponse;
            }

        } catch (IOException e) {
            failedRequests.incrementAndGet();
            log.error("调用DeepSeek聊天API失败", e);
            throw new DeepSeekException(500, "NETWORK_ERROR", "NetworkError",
                    "网络请求失败: " + e.getMessage(), e);
        } catch (Exception e) {
            failedRequests.incrementAndGet();
            log.error("聊天API调用异常", e);
            throw new DeepSeekException(500, "UNKNOWN_ERROR", "InternalError",
                    "API调用异常: " + e.getMessage(), e);
        }
    }

    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(request), executorService)
                .exceptionally(e -> {
                    log.error("异步聊天请求失败", e);
                    throw new DeepSeekException(500, "ASYNC_ERROR", "AsyncError",
                            "异步请求失败: " + e.getMessage(), e);
                });
    }

    @Override
    public String simpleChat(String message) {
        ChatRequest request = ChatRequest.createUserRequest(config.getDefaultModel(), message);
        ChatResponse response = chat(request);
        return response.getFirstContent();
    }

    @Override
    public String chatWithSystemPrompt(String systemPrompt, String userMessage) {
        Message systemMsg = Message.createSystemMessage(systemPrompt);
        Message userMsg = Message.createUserMessage(userMessage);

        ChatRequest request = new ChatRequest(config.getDefaultModel(), Arrays.asList(systemMsg, userMsg));
        ChatResponse response = chat(request);

        return response.getFirstContent();
    }

    @Override
    public String uploadFile(UploadFileRequest uploadRequest) {
        validateUploadRequest(uploadRequest);

        try {
            // 创建多部分请求
            HttpPost httpPost = new HttpPost(config.getApiBaseUrl() + "/v1/files");
            httpPost.setHeader("Authorization", "Bearer " + config.getApiKey());

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            // 添加文件部分
            if (uploadRequest.getFile() != null) {
                builder.addBinaryBody("file", uploadRequest.getFile(),
                        ContentType.create(uploadRequest.inferMimeType()),
                        uploadRequest.getFileName());
            } else if (uploadRequest.getInputStream() != null) {
                builder.addBinaryBody("file", uploadRequest.getInputStream(),
                        ContentType.create(uploadRequest.inferMimeType()),
                        uploadRequest.getFileName());
            } else if (uploadRequest.getFileBytes() != null) {
                builder.addBinaryBody("file", uploadRequest.getFileBytes(),
                        ContentType.create(uploadRequest.inferMimeType()),
                        uploadRequest.getFileName());
            } else if (uploadRequest.getBase64Content() != null) {
                byte[] fileBytes = Base64.getDecoder().decode(uploadRequest.getBase64Content());
                builder.addBinaryBody("file", fileBytes,
                        ContentType.create(uploadRequest.inferMimeType()),
                        uploadRequest.getFileName());
            }

            // 添加用途参数
            if (uploadRequest.getPurpose() != null) {
                builder.addTextBody("purpose", uploadRequest.getPurpose());
            }

            httpPost.setEntity(builder.build());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return handleUploadResponse(response);
            }

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new DeepSeekException(500, "UPLOAD_ERROR", "UploadError",
                    "文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadFile(File file, String purpose) {
        UploadFileRequest request = new UploadFileRequest();
        request.setFile(file);
        request.setFileName(file.getName());
        request.setPurpose(purpose);

        return uploadFile(request);
    }

    @Override
    public String analyzeFiles(FileAnalysisRequest analysisRequest) {
        validateAnalysisRequest(analysisRequest);

        try {
            // 创建包含文件引用的聊天请求
            List<Message> messages = new ArrayList<>();

            // 添加系统消息（如果有指令）
            if (analysisRequest.getInstruction() != null) {
                messages.add(Message.createSystemMessage(analysisRequest.getInstruction()));
            }

            // 添加用户消息（包含文件引用）
            StringBuilder userMessage = new StringBuilder();
            if (analysisRequest.getQuestion() != null) {
                userMessage.append(analysisRequest.getQuestion()).append("\n\n");
            }
            userMessage.append("请分析以下文件：");

            for (String fileId : analysisRequest.getFileIds()) {
                userMessage.append("\n- 文件ID: ").append(fileId);
            }

            messages.add(Message.createUserMessage(userMessage.toString()));

            // 创建聊天请求
            ChatRequest chatRequest = new ChatRequest(config.getDefaultModel(), messages);

            // 如果有输出格式要求
            if (analysisRequest.getOutputFormat() != null) {
                chatRequest.addSystemPrompt("请以" + analysisRequest.getOutputFormat() + "格式回复");
            }

            // 调用聊天API
            ChatResponse response = chat(chatRequest);
            return response.getFirstContent();

        } catch (Exception e) {
            log.error("文件分析失败", e);
            throw new DeepSeekException(500, "ANALYSIS_ERROR", "AnalysisError",
                    "文件分析失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadAndAnalyzeFile(File file, String instruction) {
        // 上传文件
        String fileId = uploadFile(file, "analysis");

        // 分析文件
        FileAnalysisRequest analysisRequest = new FileAnalysisRequest(
                Collections.singletonList(fileId), instruction);

        return analyzeFiles(analysisRequest);
    }

    @Override
    public List<String> batchUploadFiles(List<File> files, String purpose) {
        List<String> fileIds = new ArrayList<>();
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (File file : files) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return uploadFile(file, purpose);
                } catch (Exception e) {
                    log.error("批量上传文件失败: {}", file.getName(), e);
                    return null;
                }
            }, executorService);

            futures.add(future);
        }

        // 等待所有上传完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        for (CompletableFuture<String> future : futures) {
            try {
                String fileId = future.get();
                if (fileId != null) {
                    fileIds.add(fileId);
                }
            } catch (Exception e) {
                log.error("获取上传结果失败", e);
            }
        }

        return fileIds;
    }

    @Override
    public DeepSeekConfig getConfig() {
        return config;
    }

    @Override
    public boolean testConnection() {
        try {
            // 发送一个简单的测试请求
            String response = simpleChat("Hello");
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            log.error("连接测试失败", e);
            return false;
        }
    }

    @Override
    public String getUsageStatistics() {
        int successRequests = totalRequests.get() - failedRequests.get();
        double successRate = totalRequests.get() > 0 ?
                (successRequests * 100.0 / totalRequests.get()) : 0;

        double avgResponseTime = totalRequests.get() > 0 ?
                totalResponseTime.get() * 1.0 / totalRequests.get() : 0;

        return String.format(
                "DeepSeek API 使用统计:\n" +
                "总请求数: %d\n" +
                "成功请求: %d\n" +
                "失败请求: %d\n" +
                "成功率: %.2f%%\n" +
                "总token使用量: %d\n" +
                "平均响应时间: %.2fms\n" +
                "活跃线程数: %d",
                totalRequests.get(),
                successRequests,
                failedRequests.get(),
                successRate,
                totalTokensUsed.get(),
                avgResponseTime,
                Thread.activeCount()
        );
    }

    @Override
    public void shutdown() {
        try {
            log.info("开始关闭DeepSeek服务...");

            // 关闭执行器
            if (executorService != null) {
                executorService.shutdown();
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            }

            // 关闭HTTP客户端
            if (httpClient != null) {
                httpClient.close();
            }

            // 关闭连接管理器
            if (connectionManager != null) {
                connectionManager.close();
            }

            log.info("DeepSeek服务已关闭");
            log.info("最终统计信息:\n{}", getUsageStatistics());

        } catch (Exception e) {
            log.error("关闭服务时发生错误", e);
        }
    }

    // ========== 私有方法 ==========

    private HttpPost createChatHttpPost(ChatRequest request) throws IOException {
        HttpPost httpPost = new HttpPost(config.getChatCompletionsUrl());

        // 设置Header
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + config.getApiKey());
        httpPost.setHeader("Accept", "application/json");

        // 添加自定义Header（可选）
        httpPost.setHeader("X-Request-ID", UUID.randomUUID().toString());

        // 设置请求体
        String requestBody = objectMapper.writeValueAsString(request);
        if (config.isEnableVerboseLogging()) {
            log.debug("请求体: {}", requestBody);
        }

        httpPost.setEntity(new StringEntity(requestBody, StandardCharsets.UTF_8));
        return httpPost;
    }

    private ChatResponse handleChatResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);

        if (config.isEnableVerboseLogging()) {
            log.debug("响应状态码: {}, 响应体: {}", statusCode, responseBody);
        }

        if (statusCode == HttpStatus.SC_OK) {
            try {
                return objectMapper.readValue(responseBody, ChatResponse.class);
            } catch (IOException e) {
                // 尝试记录原始响应以帮助调试
                log.warn("解析响应失败，原始响应: {}", responseBody);

                // 尝试使用更宽松的方式解析
                try {
                    // 使用Map读取，然后手动构造对象
                    Map<String, Object> rawResponse = objectMapper.readValue(responseBody, Map.class);
                    return parseRawResponse(rawResponse);
                } catch (IOException ex) {
                    log.error("解析聊天响应失败", e);
                    throw new DeepSeekException(500, "PARSE_ERROR", "ParseError",
                            "解析响应失败: " + e.getMessage(), e);
                }
            }
        } else {
            handleErrorResponse(statusCode, responseBody, "聊天");
        }

        return null;
    }

    private ChatResponse parseRawResponse(Map<String, Object> rawResponse) {
        ChatResponse response = new ChatResponse();

        // 手动设置已知字段
        response.setId((String) rawResponse.get("id"));
        response.setObject((String) rawResponse.get("object"));
        response.setCreated((Long) rawResponse.get("created"));
        response.setModel((String) rawResponse.get("model"));
        response.setSystemFingerprint((String) rawResponse.get("system_fingerprint"));

        // 处理usage字段
        if (rawResponse.get("usage") instanceof Map) {
            Map<String, Object> usageMap = (Map<String, Object>) rawResponse.get("usage");
            ChatResponse.Usage usage = new ChatResponse.Usage();

            usage.setPromptTokens((Integer) usageMap.get("prompt_tokens"));
            usage.setCompletionTokens((Integer) usageMap.get("completion_tokens"));
            usage.setTotalTokens((Integer) usageMap.get("total_tokens"));

            response.setUsage(usage);
        }

        return response;
    }

    private String handleUploadResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        if (statusCode == HttpStatus.SC_OK) {
            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                String fileId = jsonNode.path("id").asText();

                log.info("文件上传成功，文件ID: {}", fileId);
                return fileId;

            } catch (IOException e) {
                log.error("解析上传响应失败", e);
                throw new DeepSeekException(500, "PARSE_ERROR", "ParseError",
                        "解析上传响应失败", e);
            }
        } else {
            handleErrorResponse(statusCode, responseBody, "文件上传");
        }

        return null;
    }

    private void handleErrorResponse(int statusCode, String responseBody, String operation) {
        log.error("{} API调用失败，状态码: {}, 响应: {}", operation, statusCode, responseBody);

        try {
            JsonNode errorNode = objectMapper.readTree(responseBody);
            String errorCode = errorNode.path("error").path("code").asText("UNKNOWN_ERROR");
            String errorType = errorNode.path("error").path("type").asText("ApiError");
            String errorMessage = errorNode.path("error").path("message").asText("API调用失败");

            throw new DeepSeekException(statusCode, errorCode, errorType, errorMessage);

        } catch (IOException e) {
            throw new DeepSeekException(statusCode, "PARSE_ERROR", "ParseError",
                    operation + "失败，状态码: " + statusCode);
        }
    }

    private void validateUploadRequest(UploadFileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("上传请求不能为null");
        }

        // 检查文件内容来源
        int contentSources = 0;
        if (request.getFile() != null) contentSources++;
        if (request.getInputStream() != null) contentSources++;
        if (request.getFileBytes() != null) contentSources++;
        if (request.getBase64Content() != null) contentSources++;

        if (contentSources != 1) {
            throw new IllegalArgumentException("必须且只能指定一种文件内容来源");
        }

        // 检查文件名
        if (request.getFileName() == null || request.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 检查文件大小
        if (request.getFile() != null && request.getFile().length() > request.getMaxFileSize()) {
            throw new IllegalArgumentException("文件大小超过限制: " + request.getMaxFileSize() + "字节");
        }

        // 检查文件格式
        if (!request.isFileFormatSupported()) {
            throw new IllegalArgumentException("不支持的文件格式: " + request.getFileExtension());
        }
    }

    private void validateAnalysisRequest(FileAnalysisRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("分析请求不能为null");
        }

        if (request.getFileIds() == null || request.getFileIds().isEmpty()) {
            throw new IllegalArgumentException("文件ID列表不能为空");
        }

        if (request.getInstruction() == null || request.getInstruction().trim().isEmpty()) {
            throw new IllegalArgumentException("分析指令不能为空");
        }
    }
}
