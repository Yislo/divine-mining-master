package com.divine.admin.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * 文件分析请求实体类
 * 用于分析已上传文件的内容
 */
@Data
public class FileAnalysisRequest {

    /**
     * 文件ID列表
     * 已上传文件的唯一标识符列表
     */
    @JsonProperty("file_ids")
    private List<String> fileIds;

    /**
     * 分析指令
     * 告诉模型如何处理文件内容
     * 例如: "总结这篇文档", "提取关键信息", "翻译成英文"
     */
    @JsonProperty("instruction")
    private String instruction;

    /**
     * 附加问题
     * 基于文件内容提出的具体问题
     */
    @JsonProperty("question")
    private String question;

    /**
     * 输出格式要求
     * 例如: "markdown", "json", "plain text"
     */
    @JsonProperty("output_format")
    private String outputFormat;

    /**
     * 是否提取关键信息
     */
    @JsonProperty("extract_key_points")
    private boolean extractKeyPoints = true;

    /**
     * 是否生成摘要
     */
    @JsonProperty("generate_summary")
    private boolean generateSummary = false;

    /**
     * 摘要最大长度（单词数）
     */
    @JsonProperty("summary_max_length")
    private Integer summaryMaxLength = 200;

    /**
     * 是否翻译内容
     */
    @JsonProperty("translate")
    private boolean translate = false;

    /**
     * 目标语言（如果翻译）
     * 例如: "en", "zh", "ja", "ko"
     */
    @JsonProperty("target_language")
    private String targetLanguage;

    /**
     * 构造方法
     */
    public FileAnalysisRequest(List<String> fileIds, String instruction) {
        this.fileIds = fileIds;
        this.instruction = instruction;
    }
}
