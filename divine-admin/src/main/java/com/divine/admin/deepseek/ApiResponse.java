package com.divine.admin.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

/**
 * 通用API响应包装类
 * 可以处理未知字段
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)  // 忽略所有未知属性
public class ApiResponse<T> {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private Long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private T choices;

    @JsonProperty("usage")
    private Map<String, Object> usage;  // 使用Map接收不确定结构的字段

    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    // 其他未知字段会被自动忽略
}
