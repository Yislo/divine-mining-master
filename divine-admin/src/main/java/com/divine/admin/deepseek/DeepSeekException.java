package com.divine.admin.deepseek;

import lombok.Getter;

/**
 * DeepSeek API调用异常类
 * 包含详细的错误信息
 */
@Getter
public class DeepSeekException extends RuntimeException {

    /**
     * HTTP状态码
     */
    private final int statusCode;

    /**
     * API错误代码
     */
    private final String errorCode;

    /**
     * 错误类型
     */
    private final String errorType;

    /**
     * 请求ID（如果可用）
     */
    private final String requestId;

    public DeepSeekException(String message) {
        super(message);
        this.statusCode = 500;
        this.errorCode = "UNKNOWN_ERROR";
        this.errorType = "InternalError";
        this.requestId = null;
    }

    public DeepSeekException(int statusCode, String errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorType = "ApiError";
        this.requestId = null;
    }

    public DeepSeekException(int statusCode, String errorCode, String errorType, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.requestId = null;
    }

    public DeepSeekException(int statusCode, String errorCode, String errorType,
                           String message, String requestId) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.requestId = requestId;
    }

    public DeepSeekException(int statusCode, String errorCode, String errorType,
                           String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.requestId = null;
    }

    /**
     * 获取完整的错误信息
     */
    public String getFullErrorMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeepSeek API Error: ").append(getMessage()).append("\n");
        sb.append("Status Code: ").append(statusCode).append("\n");
        sb.append("Error Code: ").append(errorCode).append("\n");
        sb.append("Error Type: ").append(errorType).append("\n");
        if (requestId != null) {
            sb.append("Request ID: ").append(requestId).append("\n");
        }
        return sb.toString();
    }
}
